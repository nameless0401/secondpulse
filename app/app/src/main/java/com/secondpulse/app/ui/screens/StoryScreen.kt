package com.secondpulse.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.secondpulse.app.domain.ReaderMode
import com.secondpulse.app.ui.*
import com.secondpulse.app.ui.components.*
import com.secondpulse.app.ui.theme.SpColors
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable fun StoryScreen(state: AppUiState.Ready, vm: GameViewModel) {
    val reading = state.game.reading
    val listState = rememberLazyListState(reading.scrollIndex, reading.scrollOffset)
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .distinctUntilChanged().debounce(250).collect { (i, o) -> vm.updateReading { it.copy(scrollIndex=i, scrollOffset=o) } }
    }
    Column(Modifier.fillMaxSize()) {
        SpTopHeader("SECOND PULSE", state.block.temporalContext)
        Row(Modifier.fillMaxWidth().padding(horizontal=18.dp), horizontalArrangement = Arrangement.End) {
            FilterChip(reading.readerMode == ReaderMode.SCROLL, { vm.updateReading { it.copy(readerMode = ReaderMode.SCROLL) } }, { Text("Défilement") })
            Spacer(Modifier.width(8.dp))
            FilterChip(reading.readerMode == ReaderMode.PAGINATION, { vm.updateReading { it.copy(readerMode = ReaderMode.PAGINATION) } }, { Text("Pagination") })
        }
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(start=18.dp,end=18.dp,bottom=24.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            items(state.block.paragraphs) { p -> Text(p, style = MaterialTheme.typography.bodyLarge, color = SpColors.TextSecondary) }
            state.block.choice?.let { choice ->
                item { Text(choice.prompt, style = MaterialTheme.typography.titleLarge, color = SpColors.TextPrimary) }
                items(state.visibleOptions, key={it.id}) { option -> SpChoiceCard(option, true) { vm.select(choice.id, option.id) } }
            }
        }
    }
}
