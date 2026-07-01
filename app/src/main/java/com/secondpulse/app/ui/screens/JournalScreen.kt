package com.secondpulse.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.secondpulse.app.ui.*
import com.secondpulse.app.ui.components.*
import com.secondpulse.app.ui.theme.SpColors

@Composable fun JournalScreen(state: AppUiState.Ready, vm: GameViewModel) {
    Column(Modifier.fillMaxSize()) {
        SpTopHeader("JOURNAL", state.block.temporalContext)
        LazyColumn(Modifier.fillMaxSize(), contentPadding=PaddingValues(18.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) {
            if (state.game.journal.isEmpty()) item { Text("Aucune conséquence manifestée n’a encore été consignée.", style=MaterialTheme.typography.bodyLarge) }
            items(state.game.journal) { e -> SpCard(Modifier.fillMaxWidth()) { Text(e.title,style=MaterialTheme.typography.titleLarge,color=SpColors.TextPrimary); Text(e.body) } }
        }
    }
}
