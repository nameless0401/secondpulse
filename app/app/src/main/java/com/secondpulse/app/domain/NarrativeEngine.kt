package com.secondpulse.app.domain

class NarrativeException(message: String) : IllegalStateException(message)

data class ChoiceResolution(val state: GameState, val nextBlock: NarrativeBlock)

class ConditionEvaluator {
    fun evaluate(condition: Condition, state: GameState): Boolean = when (condition) {
        Condition.Always -> true
        is Condition.All -> condition.conditions.all { evaluate(it, state) }
        is Condition.Any -> condition.conditions.any { evaluate(it, state) }
        is Condition.Not -> !evaluate(condition.condition, state)
        is Condition.VarEquals -> state.variables[condition.key] == condition.value
        is Condition.ChoiceMade -> state.choiceLedger.any { it.choiceId == condition.choiceId && (condition.optionId == null || it.optionId == condition.optionId) }
        is Condition.RouteAtLeast -> (state.routeStates[condition.routeId]?.phase ?: RoutePhase.UNAVAILABLE).ordinal >= condition.phase.ordinal
        is Condition.Encountered -> condition.characterId in state.encounteredCharacters
        is Condition.Era -> state.eraScope == condition.value
    }
}

class StateValidator(private val content: StoryContent) {
    private val blocks = content.blocks.associateBy { it.id }
    fun validate(state: GameState) {
        require(state.schemaVersion == GameState.CURRENT_SCHEMA) { "Schema actif incohérent" }
        require(blocks.containsKey(state.reading.blockId)) { "Bloc courant inconnu: ${state.reading.blockId}" }
        state.choiceLedger.groupBy { it.choiceId }.forEach { (id, records) -> require(records.size == 1) { "Choix résolu plusieurs fois: $id" } }
        state.routeStates.filterValues { it.phase == RoutePhase.ENGAGED }.keys.forEach { route ->
            val forbidden = content.incompatibleRoutes[route].orEmpty()
            require(forbidden.none { state.routeStates[it]?.phase == RoutePhase.ENGAGED }) { "Routes incompatibles engagées: $route / $forbidden" }
        }
        state.communications.values.forEach { require(it.eraScope == state.eraScope || it.status == CommunicationStatus.ARCHIVED) { "Communication d'une ère inactive visible" } }
    }
}

class NarrativeEngine(private val content: StoryContent) {
    private val blocks = content.blocks.associateBy { it.id }
    private val evaluator = ConditionEvaluator()
    private val validator = StateValidator(content)

    fun block(id: String): NarrativeBlock = blocks[id] ?: throw NarrativeException("Bloc introuvable: $id")

    fun visibleOptions(choice: Choice, state: GameState): List<Option> =
        choice.options.filter { evaluator.evaluate(it.eligibility, state) }

    fun resolve(state: GameState, choiceId: String, optionId: String): ChoiceResolution {
        val current = block(state.reading.blockId)
        val choice = current.choice ?: throw NarrativeException("Aucun choix dans ${current.id}")
        if (choice.id != choiceId) throw NarrativeException("Choix hors contexte")
        if (choice.singleUse && state.choiceLedger.any { it.choiceId == choiceId }) throw NarrativeException("Choix déjà résolu")
        val option = visibleOptions(choice, state).singleOrNull { it.id == optionId }
            ?: throw NarrativeException("Option absente ou inéligible")

        var candidate = state.copy(sequence = state.sequence + 1)
        option.effects.forEach { effect -> candidate = apply(effect, candidate, option.id) }
        val next = block(option.nextBlockId)
        candidate = candidate.copy(
            contentVersion = content.contentVersion,
            choiceLedger = candidate.choiceLedger + ChoiceRecord(choiceId, optionId, candidate.sequence),
            reading = candidate.reading.copy(
                actId = next.actId, chapterId = next.chapterId, sceneId = next.sceneId,
                blockId = next.id, anchorId = next.anchorId, scrollIndex = 0, scrollOffset = 0, pageIndex = 0,
            )
        )
        validator.validate(candidate)
        return ChoiceResolution(candidate, next)
    }

    private fun apply(effect: Effect, state: GameState, sourceOptionId: String): GameState = when (effect) {
        is Effect.SetVariable -> state.copy(variables = state.variables + (effect.key to effect.value))
        is Effect.IncrementInt -> {
            val old = (state.variables[effect.key] as? Value.IntVal)?.value ?: 0
            state.copy(variables = state.variables + (effect.key to Value.IntVal(old + effect.delta)))
        }
        is Effect.EncounterCharacter -> state.copy(encounteredCharacters = state.encounteredCharacters + effect.characterId)
        is Effect.TransitionRoute -> {
            val old = state.routeStates[effect.routeId] ?: RouteState()
            state.copy(routeStates = state.routeStates + (effect.routeId to old.copy(
                phase = effect.phase, evidence = old.evidence + effect.evidenceId, lastChangedBy = effect.evidenceId
            )))
        }
        is Effect.ScheduleConsequence -> state.copy(consequenceQueue = state.consequenceQueue + ScheduledConsequence(effect.consequenceId, sourceOptionId, effect.dueAnchorId))
        is Effect.AddJournal -> state.copy(journal = state.journal + JournalEntry(effect.id, effect.title, effect.body, effect.sourceEventIds))
        is Effect.UnlockCommunication -> state.copy(communications = state.communications + (effect.id to CommunicationState(effect.id, CommunicationStatus.UNREAD, effect.eraScope)))
        is Effect.SetEra -> state.copy(eraScope = effect.eraScope, communications = state.communications.filterValues { it.eraScope == effect.eraScope })
    }
}
