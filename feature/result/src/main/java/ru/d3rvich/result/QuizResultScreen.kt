package ru.d3rvich.result

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.result.model.QuizResultUiAction
import ru.d3rvich.result.model.QuizResultUiState
import ru.d3rvich.result.view.QuizResultDetailView

@Composable
fun QuizResultScreen(
    navigateToQuiz: (quizId: Long) -> Unit,
    navigateToStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: QuizResultViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    QuizResultScreen(state = state, onRetryClick = navigateToQuiz, modifier = modifier)
    val context = LocalContext.current
    LaunchedEffect(viewModel) {
        viewModel.uiAction.collect { action ->
            when (action) {
                QuizResultUiAction.ShowErrorAndNavigateToStart -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_message),
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToStart()
                }
            }
        }
    }
}

@Composable
private fun QuizResultScreen(
    state: QuizResultUiState,
    onRetryClick: (quizId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        state,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { state ->
        when (state) {
            QuizResultUiState.Loading -> {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        color = Color(0xFFBCB7FF),
                        trackColor = Color.White,
                    )
                }
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