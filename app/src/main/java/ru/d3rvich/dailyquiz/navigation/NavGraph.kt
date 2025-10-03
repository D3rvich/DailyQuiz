package ru.d3rvich.dailyquiz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.json.Json
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty
import ru.d3rvich.history.HistoryScreen
import ru.d3rvich.quiz.screens.FiltersScreen
import ru.d3rvich.quiz.screens.QuizScreen
import ru.d3rvich.quiz.screens.ResultsScreen
import ru.d3rvich.quiz.screens.StartScreen
import ru.d3rvich.result.QuizResultScreen
import ru.d3rvich.ui.model.QuizResultUiModel
import ru.d3rvich.ui.navigation.Screen
import ru.d3rvich.ui.navigation.Screens

@Composable
internal fun Nav3Graph(modifier: Modifier = Modifier) {
    val backStack = rememberSaveable { mutableStateListOf<Screen>(Screens.QuizMain.Start) }

    NavDisplay(
        backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            // Add the default decorators for managing scenes and saving state
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            // Then add the view model store decorator
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            quizEntry(
                navigateBackToStart = {
                    while (backStack.last() != Screens.QuizMain.Start) {
                        if (backStack.removeLastOrNull() == null) {
                            backStack.add(Screens.QuizMain.Start)
                            break
                        }
                    }
                },
                navigateToFilters = { backStack.add(Screens.QuizMain.Filters) },
                navigateToHistory = { backStack.add(Screens.History) },
                navigateToQuiz = { category, difficulty ->
                    backStack.add(Screens.QuizMain.Quiz(category, difficulty))
                },
                navigateToResult = { correctAnswers, totalAnswers ->
                    backStack.add(Screens.QuizMain.Result(correctAnswers, totalAnswers))
                },
                onBack = { backStack.removeLastOrNull() }
            )
            historyEntry(
                navigateToFilters = {
                    backStack.removeLastOrNull()
                    backStack.add(Screens.QuizMain.Filters)
                },
                navigateToQuizResult = {
                    backStack.add(Screens.QuizResult(Json.encodeToString(it)))
                },
                navigateBack = { backStack.removeLastOrNull() }
            )
            quizResultEntry(
                navigateToQuiz = { backStack.add(Screens.QuizMain.Quiz(quizId = it)) },
                navigateBack = { backStack.removeLastOrNull() })
        },
        modifier = modifier
    )
}

private fun EntryProviderBuilder<Screen>.quizEntry(
    navigateBackToStart: () -> Unit,
    navigateToFilters: () -> Unit,
    navigateToHistory: () -> Unit,
    navigateToQuiz: (Category, Difficulty) -> Unit,
    navigateToResult: (correctAnswers: Int, totalAnswers: Int) -> Unit,
    onBack: () -> Unit,
) {
    entry<Screens.QuizMain.Start> {
        StartScreen(
            isLoading = false,
            navigateToFilters = navigateToFilters,
            navigateToHistory = navigateToHistory
        )
    }
    entry<Screens.QuizMain.Filters> {
        var category: Category? by rememberSaveable { mutableStateOf(null) }
        var difficulty: Difficulty? by rememberSaveable { mutableStateOf(null) }
        FiltersScreen(
            category = category,
            difficulty = difficulty,
            onCategoryChange = { category = it },
            onDifficultChange = { difficulty = it },
            onStartQuiz = { navigateToQuiz(category!!, difficulty!!) },
            onBack = onBack
        )
    }
    entry<Screens.QuizMain.Quiz> { key ->
        QuizScreen(
            key = key,
            navigateToStart = navigateBackToStart,
            navigateToResult = navigateToResult,
            onBack = onBack
        )
    }
    entry<Screens.QuizMain.Result> {
        val (correctAnswers, totalAnswers) = it
        ResultsScreen(
            correctAnswers = correctAnswers,
            totalQuestions = totalAnswers,
            navigateToStart = navigateBackToStart
        )
    }
}

private fun EntryProviderBuilder<Screen>.historyEntry(
    navigateToFilters: () -> Unit,
    navigateToQuizResult: (QuizResultUiModel) -> Unit,
    navigateBack: () -> Unit
) {
    entry(Screens.History) {
        HistoryScreen(
            navigateToQuizFilters = navigateToFilters,
            navigateToQuizResult = navigateToQuizResult,
            navigateBack = navigateBack
        )
    }
}

private fun EntryProviderBuilder<Screen>.quizResultEntry(
    navigateToQuiz: (quizId: Long) -> Unit,
    navigateBack: () -> Unit
) {
    entry<Screens.QuizResult> { key ->
        val quizResult = Json.decodeFromString<QuizResultUiModel>(key.quizResultJson)
        QuizResultScreen(
            quizResult = quizResult,
            navigateToQuiz = navigateToQuiz,
            navigateBack = navigateBack
        )
    }
}