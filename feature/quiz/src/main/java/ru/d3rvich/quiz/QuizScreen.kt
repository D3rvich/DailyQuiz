package ru.d3rvich.quiz

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.quiz.model.QuizUiAction
import ru.d3rvich.quiz.model.QuizUiEvent
import ru.d3rvich.quiz.model.QuizUiState
import ru.d3rvich.quiz.views.QuizView

@Composable
internal fun QuizScreen(
    navigateToStart: () -> Unit,
    navigateToResult: (correctAnswers: Int, totalAnswers: Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<QuizNewViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    when (val state = state.value) {
        QuizUiState.Loading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is QuizUiState.Quiz -> {
            QuizView(
                modifier = modifier,
                questions = state.quiz.questions,
                currentQuestionIndex = state.currentQuestionIndex,
                selectedAnswerIndex = state.quiz.questions[state.currentQuestionIndex].selectedAnswerIndex,
                showCorrectAnswer = state.showCorrectAnswer,
                timerCurrentValue = state.timer,
                timerMaxValue = state.maxTimerValue,
                showTimeoutMessage = state.showTimeout,
                onAnswerSelect = { viewModel.obtainEvent(QuizUiEvent.OnAnswerSelected(it)) },
                onNextClick = { viewModel.obtainEvent(QuizUiEvent.OnNextClicked) },
                onRetryClick = { viewModel.obtainEvent(QuizUiEvent.OnRetryClicked) },
                onBackClick = onBackClick
            )
        }
    }
    LaunchedEffect(viewModel) {
        viewModel.uiAction.collect { action ->
            when (action) {
                QuizUiAction.NavigateToStart -> navigateToStart()

                is QuizUiAction.NavigateToResults -> navigateToResult(
                    action.correctAnswers,
                    action.totalAnswers
                )

                QuizUiAction.ShowError -> {
                    Toast.makeText(
                        context,
                        R.string.error_message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}