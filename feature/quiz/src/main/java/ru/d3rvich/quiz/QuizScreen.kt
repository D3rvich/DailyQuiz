package ru.d3rvich.quiz

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.quiz.model.QuizUiAction
import ru.d3rvich.quiz.model.QuizUiEvent
import ru.d3rvich.quiz.model.QuizUiState
import ru.d3rvich.quiz.views.FiltersView
import ru.d3rvich.quiz.views.QuestionView
import ru.d3rvich.quiz.views.ResultsView
import ru.d3rvich.quiz.views.StartView

@Composable
fun QuizScreen(navigateToHistory: () -> Unit, modifier: Modifier = Modifier) {
    val viewModel: QuizViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    QuizScreen(
        state = state,
        modifier = modifier,
        onAnswerSelected = { viewModel.obtainEvent(QuizUiEvent.OnAnswerSelected(it)) },
        onCategorySelected = { viewModel.obtainEvent(QuizUiEvent.OnCategorySelected(it)) },
        onDifficultSelected = { viewModel.obtainEvent(QuizUiEvent.OnDifficultSelected(it)) },
        onStartClick = { viewModel.obtainEvent(QuizUiEvent.OnStartClicked) },
        onNextClick = { viewModel.obtainEvent(QuizUiEvent.OnNextClicked) },
        onBackClick = { viewModel.obtainEvent(QuizUiEvent.OnBackClicked) },
        onRetryClick = { viewModel.obtainEvent(QuizUiEvent.OnRetryClicked) },
        onHistoryClick = navigateToHistory,
    )
    val context = LocalContext.current
    LaunchedEffect(state) {
        viewModel.obtainEvent(QuizUiEvent.EnterScreen)
    }
    LaunchedEffect(viewModel) {
        viewModel.uiAction.collect { action ->
            when (action) {
                QuizUiAction.ShowError -> Toast.makeText(
                    context,
                    R.string.error_message,
                    Toast.LENGTH_LONG
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuizScreen(
    state: QuizUiState,
    modifier: Modifier = Modifier,
    onAnswerSelected: (index: Int) -> Unit,
    onCategorySelected: (category: Category) -> Unit,
    onDifficultSelected: (difficult: Difficult) -> Unit,
    onStartClick: () -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    onHistoryClick: () -> Unit,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            when (state) {
                is QuizUiState.Start -> {
                    StartView(
                        isLoading = state.isLoading,
                        onStartClick = onStartClick,
                        onHistoryClick = onHistoryClick
                    )
                }

                is QuizUiState.Quiz -> {
                    QuestionView(
                        question = state.quiz.questions[state.currentQuestionIndex],
                        progressCount = state.currentQuestionIndex + 1,
                        maxQuestions = state.quiz.questions.size,
                        selectedAnswerIndex = state.quiz.questions[state.currentQuestionIndex].selectedAnswerIndex,
                        onAnswerSelect = onAnswerSelected,
                        onNextClick = onNextClick,
                        onBackClick = onBackClick,
                    )
                }

                is QuizUiState.Results -> {
                    ResultsView(
                        correctAnswers = state.correctAnswers,
                        totalQuestions = state.totalQuestions,
                        onRetryClick = onRetryClick
                    )
                }

                is QuizUiState.Filters -> {
                    FiltersView(
                        category = state.selectedCategory,
                        difficult = state.selectedDifficult,
                        onCategoryChange = onCategorySelected,
                        onDifficultChange = onDifficultSelected,
                        onStartClick = onStartClick
                    )
                }
            }
        }
    }
}