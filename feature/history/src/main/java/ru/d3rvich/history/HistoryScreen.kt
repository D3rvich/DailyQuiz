package ru.d3rvich.history

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.history.model.HistoryUiEvent
import ru.d3rvich.history.model.HistoryUiState
import ru.d3rvich.history.views.HistoryEmptyView
import ru.d3rvich.history.views.QuizHistoryView

@Composable
fun HistoryScreen(
    navigateToQuiz: () -> Unit,
    navigateToQuizDebriefing: (quizId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: HistoryViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HistoryScreen(
        state = state,
        modifier = modifier,
        onRemoveQuiz = { viewModel.obtainEvent(HistoryUiEvent.OnRemoveQuiz(it)) },
        onStartQuizClick = { navigateToQuiz() },
        onQuizClick = { navigateToQuizDebriefing(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryScreen(
    state: HistoryUiState,
    onStartQuizClick: () -> Unit,
    onQuizClick: (quizId: Long) -> Unit,
    onRemoveQuiz: (quiz: QuizResultEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        HistoryUiState.Idle -> { /* Do nothing */
        }

        is HistoryUiState.Content -> {
            if (state.quizResultEntities.isEmpty()) {
                HistoryEmptyView(onStartQuizClick, modifier = modifier)
            } else {
                QuizHistoryView(
                    quizList = state.quizResultEntities,
                    onQuizCLick = onQuizClick,
                    modifier = modifier
                )
            }
        }
    }
}