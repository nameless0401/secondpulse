package com.secondpulse.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.secondpulse.app.ui.*
import com.secondpulse.app.ui.components.*
import com.secondpulse.app.ui.theme.SpColors

@Composable fun DossiersScreen(state: AppUiState.Ready, vm: GameViewModel) {
    var trajectory by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        SpTopHeader(if (trajectory) "TRAJECTOIRE" else "DOSSIERS VIVANTS", if (trajectory) "Évolution actuelle" else "Personnages rencontrés")
        Row(Modifier.fillMaxWidth().padding(horizontal=18.dp), horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            FilterChip(!trajectory,{trajectory=false},{Text("Personnes")}); FilterChip(trajectory,{trajectory=true},{Text("Trajectoire")})
        }
        if (trajectory) TrajectoryContent() else PeopleContent(state)
    }
}

@Composable private fun PeopleContent(state: AppUiState.Ready) {
    val people = buildList { add("Mira Vale"); if ("CHAR-SELENE" in state.game.encounteredCharacters) add("Sélène Ardan") }
    LazyColumn(Modifier.fillMaxSize(), contentPadding=PaddingValues(18.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item { Row(horizontalArrangement=Arrangement.spacedBy(6.dp)) { listOf("Famille","Promo","Admin","Autres").forEachIndexed { i,t -> FilterChip(i==0,{}, {Text(t)}) } } }
        items(people.size) { index -> val name=people[index]; SpCard(Modifier.fillMaxWidth()) { Row(verticalAlignment=Alignment.CenterVertically) { Monogram(name); Spacer(Modifier.width(16.dp)); Column { Text(name,style=MaterialTheme.typography.headlineMedium,color=SpColors.TextPrimary); Text(if(name.startsWith("Mira")) "Famille · Une présence qui demande de la vérité." else "Promo · Une confiance encore prudente.") } } } }
    }
}

@Composable private fun TrajectoryContent() {
    val cards = listOf("VISIBILITÉ" to "Ta présence commence à être remarquée.", "MÉTHODE" to "Tu avances avec rigueur sans confondre contrôle et soin.", "RÉPUTATION" to "Les perceptions restent contrastées.", "PRESSIONS ACTIVES" to "Famille et études se disputent ton attention.", "ÉQUILIBRE PERSONNEL" to "Le repos reste fragile.")
    LazyColumn(Modifier.fillMaxSize(), contentPadding=PaddingValues(18.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) { items(cards.size) { i -> SpCard(Modifier.fillMaxWidth().heightIn(min=132.dp)) { Text(cards[i].first,style=MaterialTheme.typography.headlineMedium,color=SpColors.TextPrimary); Text(cards[i].second,style=MaterialTheme.typography.bodyLarge) } } }
}
