package com.secondpulse.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.secondpulse.app.domain.SecondaryScreen
import com.secondpulse.app.ui.components.PaperBackground
import com.secondpulse.app.ui.components.SpBottomNavigation
import com.secondpulse.app.ui.screens.*

@Composable fun SecondPulseRoot(vm: GameViewModel = viewModel()) {
    val ui by vm.ui.collectAsState()
    PaperBackground {
        when (val state = ui) {
            AppUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            is AppUiState.Error -> Text(state.message, Modifier.align(Alignment.Center))
            is AppUiState.Ready -> Column(Modifier.fillMaxSize()) {
                Box(Modifier.weight(1f)) {
                    when (state.game.reading.secondaryScreen) {
                        SecondaryScreen.STORY -> StoryScreen(state, vm)
                        SecondaryScreen.PHONE -> PhoneScreen(state, vm)
                        SecondaryScreen.CAMPUS -> CampusScreen(state, vm)
                        SecondaryScreen.DOSSIERS -> DossiersScreen(state, vm)
                        SecondaryScreen.JOURNAL -> JournalScreen(state, vm)
                        SecondaryScreen.SETTINGS -> JournalScreen(state, vm)
                    }
                }
                SpBottomNavigation(state.game.reading.secondaryScreen) { vm.navigate(it) }
            }
        }
    }
}
