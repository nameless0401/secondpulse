package com.secondpulse.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.secondpulse.app.domain.EraScope
import com.secondpulse.app.ui.*
import com.secondpulse.app.ui.components.*
import com.secondpulse.app.ui.theme.SpColors

@Composable fun CampusScreen(state: AppUiState.Ready, vm: GameViewModel) {
    val academic = state.game.eraScope == EraScope.RESTARTED_ACADEMIC
    Column(Modifier.fillMaxSize()) {
        SpTopHeader("CAMPUS", if (academic) "Espace étudiant" else "Agenda")
        LazyColumn(Modifier.fillMaxSize(), contentPadding=PaddingValues(18.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) {
            item { SpCard(Modifier.fillMaxWidth()) { Text("AGENDA", style=MaterialTheme.typography.titleLarge,color=SpColors.TextPrimary); Text("Aucune échéance non sourcée n’est affichée.") } }
            if (academic) {
                item { Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.spacedBy(8.dp)) { listOf("Notes & copies","Classement","Documents","Agenda").forEach { label -> SpCard(Modifier.weight(1f).heightIn(min=92.dp)) { Text(label, color=SpColors.TextPrimary) } } } }
                item { SpCard(Modifier.fillMaxWidth()) { Text("CLASSEMENT ANONYMISÉ", style=MaterialTheme.typography.titleLarge,color=SpColors.TextPrimary); Text("1 — Étudiant 0417 — 16,12\n2 — Étudiant 0231 — 15,48\n3 — Étudiant 0189 — 15,07") } }
            }
        }
    }
}
