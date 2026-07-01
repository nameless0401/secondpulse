package com.secondpulse.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.secondpulse.app.data.AssetContentRepository
import com.secondpulse.app.data.AtomicSaveRepository
import com.secondpulse.app.domain.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

sealed interface AppUiState {
    data object Loading : AppUiState
    data class Ready(val game: GameState, val block: NarrativeBlock, val visibleOptions: List<Option>) : AppUiState
    data class Error(val message: String) : AppUiState
}

class GameViewModel(app: Application) : AndroidViewModel(app) {
    private val json = Json { ignoreUnknownKeys = false; classDiscriminator = "type"; prettyPrint = true }
    private val contentRepo = AssetContentRepository(app, json)
    private val saveRepo = AtomicSaveRepository(app, json)
    private val mutex = Mutex()
    private lateinit var engine: NarrativeEngine
    private val _ui = MutableStateFlow<AppUiState>(AppUiState.Loading)
    val ui: StateFlow<AppUiState> = _ui.asStateFlow()

    init { viewModelScope.launch { initialize() } }

    private suspend fun initialize() {
        runCatching {
            val content = contentRepo.load()
            engine = NarrativeEngine(content)
            val loaded = saveRepo.loadOrNull()
            val state = loaded ?: GameState(contentVersion = content.contentVersion, reading = ReadingPosition(blockId = content.startBlockId))
            publish(state)
        }.onFailure { _ui.value = AppUiState.Error(it.message ?: "Erreur d'initialisation") }
    }

    fun select(choiceId: String, optionId: String) = viewModelScope.launch {
        mutex.withLock {
            val ready = _ui.value as? AppUiState.Ready ?: return@withLock
            runCatching {
                val result = engine.resolve(ready.game, choiceId, optionId)
                saveRepo.save(result.state)
                publish(result.state)
            }.onFailure { _ui.value = AppUiState.Error(it.message ?: "Erreur de choix") }
        }
    }

    fun updateReading(transform: (ReadingPosition) -> ReadingPosition) = viewModelScope.launch {
        mutex.withLock {
            val ready = _ui.value as? AppUiState.Ready ?: return@withLock
            val state = ready.game.copy(reading = transform(ready.game.reading))
            saveRepo.save(state)
            publish(state)
        }
    }

    fun navigate(screen: SecondaryScreen) = updateReading { it.copy(secondaryScreen = screen) }

    private fun publish(state: GameState) {
        val block = engine.block(state.reading.blockId)
        _ui.value = AppUiState.Ready(state, block, block.choice?.let { engine.visibleOptions(it, state) }.orEmpty())
    }
}
