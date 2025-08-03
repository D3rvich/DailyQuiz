package ru.d3rvich.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    navigateToQuizResult: (quizId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: HistoryViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HistoryScreen(
        state = state,
        modifier = modifier,
        onRemoveQuiz = { viewModel.obtainEvent(HistoryUiEvent.OnRemoveQuiz(it)) },
        onStartQuizClick = { navigateToQuiz() },
        onQuizClick = { navigateToQuizResult(it) }
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
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (state) {
            HistoryUiState.Loading -> {
                CircularProgressIndicator(
                    color = Color(0xFFBCB7FF),
                    trackColor = Color.White,
                )
            }

            is HistoryUiState.Content -> {
                if (state.quizResultEntities.isEmpty()) {
                    HistoryEmptyView(onStartQuizClick)
                } else {
                    QuizHistoryView(
                        quizList = state.quizResultEntities,
                        onQuizCLick = onQuizClick,
                        onRemoveQuiz = onRemoveQuiz
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.safeDrawing)
                .align(Alignment.TopStart)
        )
    }
}