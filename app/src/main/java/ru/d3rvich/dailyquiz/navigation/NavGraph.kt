package ru.d3rvich.dailyquiz.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.json.Json
import ru.d3rvich.dailyquiz.R
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty
import ru.d3rvich.history.screens.HistoryCheckerScreen
import ru.d3rvich.history.screens.HistoryScreen
import ru.d3rvich.quiz.impl.screens.FiltersScreen
import ru.d3rvich.quiz.impl.screens.QuizScreen
import ru.d3rvich.quiz.impl.screens.ResultsScreen
import ru.d3rvich.quiz.impl.screens.StartScreen
import ru.d3rvich.result.QuizResultScreen
import ru.d3rvich.ui.model.QuizResultUiModel
import ru.d3rvich.ui.navigation.Screen
import ru.d3rvich.ui.navigation.Screens

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun Nav3Graph(modifier: Modifier = Modifier) {
    val backStack = rememberSaveable { mutableStateListOf<Screen>(Screens.QuizMain.Start) }
    val listDetailStrategy = rememberListDetailSceneStrategy<Screen>()
    NavDisplay(
        backStack,
        onBack = { backStack.removeLastOrNull() },
        sceneStrategy = listDetailStrategy,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            // Then add the view model store decorator
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            quizEntry(
                navigateBackToStart = {
                    backStack.removeIf { it !is Screens.QuizMain.Start }
                    if (backStack.isEmpty()) {
                        backStack.add(Screens.QuizMain.Start)
                    }
                },
                navigateToFilters = { backStack.add(Screens.QuizMain.Filters) },
                navigateToHistory = { backStack.add(Screens.HistoryChecker) },
                navigateToQuiz = { category, difficulty ->
                    backStack.add(Screens.QuizMain.Quiz(category, difficulty))
                },
                navigateToResult = { correctAnswers, totalAnswers ->
                    backStack.add(Screens.QuizMain.Result(correctAnswers, totalAnswers))
                },
                onBack = { backStack.removeLastOrNull() }
            )
            HistoryEntry(
                navigateToHistory = {
                    backStack.removeLastOrNull()
                    backStack.add(Screens.History)
                },
                navigateToFilters = {
                    backStack.removeLastOrNull()
                    backStack.add(Screens.QuizMain.Filters)
                },
                navigateToQuizResult = { resultUiModel ->
                    backStack.removeIf { it is Screens.QuizResult }
                    backStack.add(Screens.QuizResult(Json.encodeToString(resultUiModel)))
                },
                navigateBackFromHistory = {
                    backStack.removeIf { it is Screens.History || it is Screens.QuizResult }
                }
            )
            quizResultEntry(
                navigateToQuiz = { backStack.add(Screens.QuizMain.Quiz(quizId = it)) },
                navigateBack = { backStack.removeLastOrNull() })
        },
        modifier = modifier
    )
}

private fun EntryProviderScope<Screen>.quizEntry(
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

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun EntryProviderScope<Screen>.HistoryEntry(
    navigateToHistory: () -> Unit,
    navigateToFilters: () -> Unit,
    navigateToQuizResult: (QuizResultUiModel) -> Unit,
    navigateBackFromHistory: () -> Unit
) {
    var sharedViewModelStoreOwner: ViewModelStoreOwner? by remember { mutableStateOf(null) }
    entry<Screens.HistoryChecker> {
        sharedViewModelStoreOwner = LocalViewModelStoreOwner.current
        HistoryCheckerScreen(
            navigateToHistory = navigateToHistory,
            navigateToFilters = navigateToFilters,
            navigateBack = navigateBackFromHistory
        )
    }
    entry<Screens.History>(
        metadata = ListDetailSceneStrategy.listPane {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.detail_placeholder),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    ) {
        HistoryScreen(
            navigateToQuizResult = navigateToQuizResult,
            navigateBack = navigateBackFromHistory,
            viewModelStoreOwner = checkNotNull(sharedViewModelStoreOwner)
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun EntryProviderScope<Screen>.quizResultEntry(
    navigateToQuiz: (quizId: Long) -> Unit,
    navigateBack: () -> Unit
) {
    entry<Screens.QuizResult>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) { key ->
        val quizResult = Json.decodeFromString<QuizResultUiModel>(key.quizResultJson)
        QuizResultScreen(
            quizResult = quizResult,
            navigateToQuiz = navigateToQuiz,
            navigateBack = navigateBack
        )
    }
}