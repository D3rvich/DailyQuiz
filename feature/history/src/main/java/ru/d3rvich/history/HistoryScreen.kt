package ru.d3rvich.history

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
import ru.d3rvich.history.model.HistoryUiEvent
import ru.d3rvich.history.model.HistoryUiState
import ru.d3rvich.history.views.HistoryEmptyView
import ru.d3rvich.history.views.QuizHistoryView
import ru.d3rvich.ui.model.QuizResultUiModel

@Composable
fun HistoryScreen(
    navigateToQuiz: () -> Unit,
    navigateToQuizResult: (quizResult: QuizResultUiModel) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
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
        onStartQuizClick = { navigateToQuiz() },
        onQuizClick = { navigateToQuizResult(it) },
        onBackClick = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryScreen(
    state: HistoryUiState,
    onStartQuizClick: () -> Unit,
    onSortChange: (selectedSort: SortBy) -> Unit,
    onQuizClick: (quizResult: QuizResultUiModel) -> Unit,
    onRemoveQuiz: (quizResult: QuizResultUiModel) -> Unit,
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
                    HistoryEmptyView(onStartQuizClick)
                } else {
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
}