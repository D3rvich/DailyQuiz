package ru.d3rvich.quiz

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.quiz.model.QuizUiAction
import ru.d3rvich.quiz.model.QuizUiEvent
import ru.d3rvich.quiz.model.QuizUiState
import ru.d3rvich.quiz.views.FiltersView
import ru.d3rvich.quiz.views.QuestionView
import ru.d3rvich.quiz.views.ResultsView
import ru.d3rvich.quiz.views.StartView
import ru.d3rvich.ui.navigation.Screens

fun NavGraphBuilder.quizRoute(navController: NavController) {
    navigation<Screens.QuizMain>(startDestination = Screens.QuizMain.Start) {
        startScreen(navController)
        filtersScreen(navController)
        quiz(navController)
        result(navController)
    }
}

private fun NavGraphBuilder.startScreen(navController: NavController) {
    composable<Screens.QuizMain.Start> {
        StartView(
            isLoading = false,
            onStartClick = { navController.navigate(Screens.QuizMain.Filters) },
            onHistoryClick = { navController.navigate(Screens.History) }
        )
    }
}

private fun NavGraphBuilder.filtersScreen(navController: NavController) {
    composable<Screens.QuizMain.Filters> {
        var category: Category? by rememberSaveable { mutableStateOf(null) }
        var difficult: Difficult? by rememberSaveable { mutableStateOf(null) }
        FiltersView(
            category = category,
            difficult = difficult,
            onCategoryChange = { category = it },
            onDifficultChange = { difficult = it },
            onStartClick = {
                navController.navigate(
                    Screens.QuizMain.Quiz(
                        category = category!!,
                        difficult = difficult!!
                    )
                ) {
                    popUpTo<Screens.QuizMain.Start>()
                }
            }
        )
    }
}

private fun NavGraphBuilder.quiz(navController: NavController) {
    composable<Screens.QuizMain.Quiz> {
        val viewModel = hiltViewModel<QuizNewViewModel>()
        val state = viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current
        when (val state = state.value) {
            QuizUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = Color(0xFFBCB7FF),
                        trackColor = Color.White,
                    )
                }
            }

            is QuizUiState.Quiz -> {
                QuestionView(
                    question = state.quiz.questions[state.currentQuestionIndex],
                    progressCount = state.currentQuestionIndex + 1,
                    maxQuestions = state.quiz.questions.size,
                    selectedAnswerIndex = state.quiz.questions[state.currentQuestionIndex].selectedAnswerIndex,
                    showCorrectAnswer = state.showCorrectAnswer,
                    timerCurrentValue = state.timer,
                    timerMaxValue = state.maxTimerValue,
                    showTimeoutMessage = state.showTimeout,
                    onAnswerSelect = { viewModel.obtainEvent(QuizUiEvent.OnAnswerSelected(it)) },
                    onNextClick = { viewModel.obtainEvent(QuizUiEvent.OnNextClicked) },
                    onRetryClick = { viewModel.obtainEvent(QuizUiEvent.OnRetryClicked) },
                    onBackClick = { navController.popBackStack() }
                )

            }
        }
        LaunchedEffect(viewModel) {
            viewModel.uiAction.collect { action ->
                when (action) {
                    QuizUiAction.NavigateToStart ->
                        navController.navigate(Screens.QuizMain.Start) {
                            popUpTo<Screens.QuizMain.Start>()
                        }

                    is QuizUiAction.OpenResults -> navController.navigate(
                        Screens.QuizMain.Result(
                            action.correctAnswers,
                            action.totalAnswers
                        )
                    ) {
                        popUpTo<Screens.QuizMain.Start>()
                    }

                    QuizUiAction.ShowError -> {
                        Toast.makeText(
                            context,
                            R.string.error_message,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        }
    }
}

private fun NavGraphBuilder.result(navController: NavController) {
    composable<Screens.QuizMain.Result> {
        val args = it.toRoute<Screens.QuizMain.Result>()
        ResultsView(args.correctAnswers, args.totalAnswers) {
            navController.navigate(Screens.QuizMain.Start) {
                popUpTo(Screens.QuizMain)
            }
        }
    }
}