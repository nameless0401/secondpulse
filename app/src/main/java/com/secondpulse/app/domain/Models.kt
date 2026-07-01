package com.secondpulse.app.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable enum class EraScope { ADULT_PRE_RETURN, RESTARTED_PRE_ACADEMIC_ACCESS, RESTARTED_ACADEMIC }
@Serializable enum class ReaderMode { SCROLL, PAGINATION }
@Serializable enum class RoutePhase { UNAVAILABLE, ELIGIBLE, INCLINATION, REPETITION, ENGAGEMENT_PENDING, ENGAGED, CLOSING, CLOSED, COMPLETED }
@Serializable enum class SecondaryScreen { STORY, PHONE, CAMPUS, DOSSIERS, JOURNAL, SETTINGS }
@Serializable enum class PhoneChannel { SMS, EMAIL, GROUP }
@Serializable enum class CommunicationStatus { NOT_ARRIVED, UNREAD, READ, ACTION_REQUIRED, REPLIED, EXPIRED, ARCHIVED }

@Serializable
data class ReadingPosition(
    val actId: String = "ACT-01",
    val chapterId: String = "CH-01",
    val sceneId: String = "SCN-01",
    val blockId: String = "BLOCK-001",
    val anchorId: String = "ANCHOR-001",
    val scrollIndex: Int = 0,
    val scrollOffset: Int = 0,
    val pageIndex: Int = 0,
    val readerMode: ReaderMode = ReaderMode.SCROLL,
    val secondaryScreen: SecondaryScreen = SecondaryScreen.STORY,
    val openedCommunicationId: String? = null,
)

@Serializable
data class RouteState(
    val phase: RoutePhase = RoutePhase.UNAVAILABLE,
    val evidence: Set<String> = emptySet(),
    val openness: String = "unknown",
    val lockReason: String? = null,
    val lastChangedBy: String? = null,
    val visibleManifestations: List<String> = emptyList(),
)

@Serializable data class ChoiceRecord(val choiceId: String, val optionId: String, val resolvedAtSequence: Long)
@Serializable data class ScheduledConsequence(val consequenceId: String, val sourceOptionId: String, val dueAnchorId: String)
@Serializable data class JournalEntry(val id: String, val title: String, val body: String, val sourceEventIds: List<String>)
@Serializable data class CommunicationState(val id: String, val status: CommunicationStatus, val eraScope: EraScope)

@Serializable
data class GameState(
    val schemaVersion: Int = CURRENT_SCHEMA,
    val contentVersion: String = "sample-0.1",
    val saveId: String = "autosave",
    val sequence: Long = 0,
    val eraScope: EraScope = EraScope.ADULT_PRE_RETURN,
    val reading: ReadingPosition = ReadingPosition(),
    val variables: Map<String, Value> = emptyMap(),
    val choiceLedger: List<ChoiceRecord> = emptyList(),
    val routeStates: Map<String, RouteState> = emptyMap(),
    val consequenceQueue: List<ScheduledConsequence> = emptyList(),
    val resolvedConsequences: Set<String> = emptySet(),
    val encounteredCharacters: Set<String> = emptySet(),
    val communications: Map<String, CommunicationState> = emptyMap(),
    val journal: List<JournalEntry> = emptyList(),
    val acknowledgedMilestones: Set<String> = emptySet(),
) {
    companion object { const val CURRENT_SCHEMA = 1 }
}

@Serializable
sealed interface Value {
    @Serializable @SerialName("bool") data class Bool(val value: Boolean) : Value
    @Serializable @SerialName("int") data class IntVal(val value: Int) : Value
    @Serializable @SerialName("text") data class Text(val value: String) : Value
    @Serializable @SerialName("strings") data class Strings(val value: Set<String>) : Value
}

@Serializable
data class StoryContent(
    val contentVersion: String,
    val startBlockId: String,
    val blocks: List<NarrativeBlock>,
    val incompatibleRoutes: Map<String, Set<String>> = emptyMap(),
)

@Serializable
data class NarrativeBlock(
    val id: String,
    val actId: String,
    val chapterId: String,
    val sceneId: String,
    val anchorId: String,
    val temporalContext: String,
    val paragraphs: List<String>,
    val choice: Choice? = null,
    val nextBlockId: String? = null,
)

@Serializable
data class Choice(
    val id: String,
    val prompt: String,
    val singleUse: Boolean = true,
    val saveBeforeChoice: Boolean = true,
    val saveAfterChoice: Boolean = true,
    val options: List<Option>,
)

@Serializable
data class Option(
    val id: String,
    val visibleLabel: String,
    val intentSubtitle: String? = null,
    val neutralContextLabel: String? = null,
    val eligibility: Condition = Condition.Always,
    val effects: List<Effect> = emptyList(),
    val nextBlockId: String,
    val incompatibleOptionIds: Set<String> = emptySet(),
)

@Serializable
sealed interface Condition {
    @Serializable @SerialName("always") data object Always : Condition
    @Serializable @SerialName("all") data class All(val conditions: List<Condition>) : Condition
    @Serializable @SerialName("any") data class Any(val conditions: List<Condition>) : Condition
    @Serializable @SerialName("not") data class Not(val condition: Condition) : Condition
    @Serializable @SerialName("varEquals") data class VarEquals(val key: String, val value: Value) : Condition
    @Serializable @SerialName("choiceMade") data class ChoiceMade(val choiceId: String, val optionId: String? = null) : Condition
    @Serializable @SerialName("routeAtLeast") data class RouteAtLeast(val routeId: String, val phase: RoutePhase) : Condition
    @Serializable @SerialName("encountered") data class Encountered(val characterId: String) : Condition
    @Serializable @SerialName("era") data class Era(val value: EraScope) : Condition
}

@Serializable
sealed interface Effect {
    @Serializable @SerialName("setVar") data class SetVariable(val key: String, val value: Value) : Effect
    @Serializable @SerialName("incVar") data class IncrementInt(val key: String, val delta: Int) : Effect
    @Serializable @SerialName("encounter") data class EncounterCharacter(val characterId: String) : Effect
    @Serializable @SerialName("route") data class TransitionRoute(val routeId: String, val phase: RoutePhase, val evidenceId: String) : Effect
    @Serializable @SerialName("schedule") data class ScheduleConsequence(val consequenceId: String, val dueAnchorId: String) : Effect
    @Serializable @SerialName("journal") data class AddJournal(val id: String, val title: String, val body: String, val sourceEventIds: List<String>) : Effect
    @Serializable @SerialName("communication") data class UnlockCommunication(val id: String, val eraScope: EraScope) : Effect
    @Serializable @SerialName("setEra") data class SetEra(val eraScope: EraScope) : Effect
}
