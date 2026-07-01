package com.secondpulse.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.secondpulse.app.ui.*
import com.secondpulse.app.ui.components.*
import com.secondpulse.app.ui.theme.SpColors

@Composable fun PhoneScreen(state: AppUiState.Ready, vm: GameViewModel) {
    var tab by remember { mutableIntStateOf(0) }
    val tabs = listOf("SMS", "E-mails", "Groupes")
    val threads = when(tab) { 0 -> listOf("Maman" to "N’oublie pas ton écharpe.", "Mira" to "Tu viens après le séminaire ?") ; 1 -> listOf("Faculté" to "Confirmation d’inscription") ; else -> listOf("Groupe TD 1" to "Julian : J’ai partagé le plan.") }
    Column(Modifier.fillMaxSize()) {
        SpTopHeader("TÉLÉPHONE", state.block.temporalContext)
        TabRow(tab, containerColor = androidx.compose.ui.graphics.Color.Transparent, contentColor = SpColors.Accent) { tabs.forEachIndexed { i,t -> Tab(tab==i, {tab=i}, text={Text(t)}) } }
        LazyColumn(Modifier.fillMaxSize(), contentPadding=PaddingValues(18.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) {
            items(threads) { (name, preview) -> SpCard(Modifier.fillMaxWidth()) { Row(verticalAlignment=Alignment.CenterVertically) { Monogram(name, 56); Spacer(Modifier.width(14.dp)); Column { Text(name, style=MaterialTheme.typography.titleLarge,color=SpColors.TextPrimary); Text(preview, style=MaterialTheme.typography.bodyMedium) } } } }
        }
    }
}
