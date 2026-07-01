package com.secondpulse.app.data

import android.content.Context
import android.util.AtomicFile
import com.secondpulse.app.domain.GameState
import com.secondpulse.app.domain.StoryContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class AssetContentRepository(private val context: Context, private val json: Json) {
    suspend fun load(): StoryContent = withContext(Dispatchers.IO) {
        context.assets.open("narrative/story_sample.json").bufferedReader().use { json.decodeFromString<StoryContent>(it.readText()) }
    }
}

class AtomicSaveRepository(private val context: Context, private val json: Json) {
    private val atomic by lazy { AtomicFile(File(context.filesDir, "second_pulse_autosave.json")) }

    suspend fun loadOrNull(): GameState? = withContext(Dispatchers.IO) {
        runCatching { atomic.openRead().bufferedReader().use { json.decodeFromString<GameState>(it.readText()) } }.getOrNull()
    }

    suspend fun save(state: GameState) = withContext(Dispatchers.IO) {
        val bytes = json.encodeToString(GameState.serializer(), state).encodeToByteArray()
        val stream = atomic.startWrite()
        try {
            stream.write(bytes)
            stream.fd.sync()
            atomic.finishWrite(stream)
        } catch (t: Throwable) {
            atomic.failWrite(stream)
            throw t
        }
    }
}
