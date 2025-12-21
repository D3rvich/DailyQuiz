package ru.d3rvich.history.impl.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.history.impl.HistoryViewModel
import ru.d3rvich.history.impl.model.HistoryUiState
import ru.d3rvich.history.impl.views.EmptyHistoryView

@Composable
fun HistoryCheckerScreen(
    navigateToHistory: () -> Unit,
    navigateToFilters: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: HistoryViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HistoryCheckerScreen(
        state = state,
        navigateToHistoryScreen = navigateToHistory,
        navigateToFilters = navigateToFilters,
        navigateBack = navigateBack,
        modifier = modifier
    )
}

@Composable
internal fun HistoryCheckerScreen(
    state: HistoryUiState,
    navigateToHistoryScreen: () -> Unit,
    navigateToFilters: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (state) {
            HistoryUiState.Loading -> CircularProgressIndicator()
            is HistoryUiState.Content -> {
                if (state.quizResultEntities.isNotEmpty()) {
                    navigateToHistoryScreen()
                } else {
                    EmptyHistoryView(
                        onStartQuizClick = navigateToFilters,
                        onBackClick = navigateBack
                    )
                }
            }
        }
    }
}