package ru.d3rvich.result

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.result.model.QuizResultUiState
import ru.d3rvich.result.view.QuizResultDetailView

@Composable
fun QuizResultScreen(navigateToQuiz: (quizId: Long) -> Unit, modifier: Modifier = Modifier) {
    val viewModel: QuizResultViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    QuizResultScreen(state = state, onRetryClick = navigateToQuiz, modifier = modifier)
}

@Composable
private fun QuizResultScreen(
    state: QuizResultUiState,
    onRetryClick: (quizId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedContent(state, transitionSpec = { fadeIn() togetherWith fadeOut() }) { state ->
            when (state) {
                QuizResultUiState.Loading -> {
                    CircularProgressIndicator(
                        color = Color(0xFFBCB7FF),
                        trackColor = Color.White,
                    )
                }

                is QuizResultUiState.Content -> {
                    QuizResultDetailView(
                        quizResult = state.quiz,
                        onRetryClick = onRetryClick,
                    )
                }
            }
        }
    }
}