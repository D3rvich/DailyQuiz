package ru.d3rvich.history.impl.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.domain.model.SortBy
import ru.d3rvich.history.impl.HistoryViewModel
import ru.d3rvich.history.impl.model.HistoryUiEvent
import ru.d3rvich.history.impl.model.HistoryUiState
import ru.d3rvich.history.impl.views.QuizHistoryView
import ru.d3rvich.ui.model.QuizResultUiModel

@Composable
internal fun HistoryScreen(
    navigateToEmptyHistory: () -> Unit,
    navigateToQuizResult: (quizResult: QuizResultUiModel) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: HistoryViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HistoryScreen(
        state = state,
        modifier = modifier,
        onRemoveQuiz = { viewModel.obtainEvent(HistoryUiEvent.OnRemoveQuiz(it)) },
        onSortChange = { selectedSort ->
            viewModel.obtainEvent(HistoryUiEvent.OnSortChange(selectedSort))
        },
        onQuizClick = { navigateToQuizResult(it) },
        onEmptyHistory = navigateToEmptyHistory,
        onBackClick = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryScreen(
    state: HistoryUiState,
    onSortChange: (selectedSort: SortBy) -> Unit,
    onQuizClick: (quizResult: QuizResultUiModel) -> Unit,
    onRemoveQuiz: (quizResult: QuizResultUiModel) -> Unit,
    onEmptyHistory: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (state) {
            HistoryUiState.Loading -> {
                CircularProgressIndicator()
            }

            is HistoryUiState.Content -> {
                if (state.quizResultEntities.isEmpty()) {
                    onEmptyHistory()
                }
                QuizHistoryView(
                    quizList = state.quizResultEntities,
                    selectedSort = state.selectedSort,
                    onSortChange = onSortChange,
                    onQuizCLick = onQuizClick,
                    onRemoveQuiz = onRemoveQuiz,
                    onBackClick = onBackClick
                )
            }
        }
    }
}