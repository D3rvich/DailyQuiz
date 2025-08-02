package ru.d3rvich.debriefing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.debriefing.model.DebriefingUiState

@Composable
fun DebriefingScreen(modifier: Modifier = Modifier) {
    val viewModel: DebriefingViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DebriefingScreen(state = state, modifier = modifier)
}

@Composable
private fun DebriefingScreen(state: DebriefingUiState, modifier: Modifier = Modifier) {
    when (state) {
        DebriefingUiState.Idle -> { /* Do nothing */
        }

        is DebriefingUiState.Content -> {

        }
    }
}