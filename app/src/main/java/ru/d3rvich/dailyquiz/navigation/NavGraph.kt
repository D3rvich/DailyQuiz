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
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.json.Json
import ru.d3rvich.dailyquiz.R
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty
import ru.d3rvich.history.api.navigation.HistoryNavKey
import ru.d3rvich.history.impl.screens.HistoryCheckerScreen
import ru.d3rvich.history.impl.screens.HistoryScreen
import ru.d3rvich.quiz.api.navigation.Quiz
import ru.d3rvich.quiz.impl.screens.FiltersScreen
import ru.d3rvich.quiz.impl.screens.QuizScreen
import ru.d3rvich.quiz.impl.screens.ResultsScreen
import ru.d3rvich.quiz.impl.screens.StartScreen
import ru.d3rvich.result.api.navigation.HistoryDetailNavKey
import ru.d3rvich.result.impl.QuizResultScreen
import ru.d3rvich.ui.model.QuizResultUiModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun Nav3Graph(modifier: Modifier = Modifier) {
    val backStack = rememberSaveable { mutableStateListOf<NavKey>(Quiz.StartNavKey) }
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()
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
                    backStack.removeIf { it !is Quiz.StartNavKey }
                    if (backStack.isEmpty()) {
                        backStack.add(Quiz.StartNavKey)
                    }
                },
                navigateToFilters = { backStack.add(Quiz.FiltersNavKey) },
                navigateToHistory = { backStack.add(HistoryNavKey.CheckerNavKey) },
                navigateToQuiz = { category, difficulty ->
                    backStack.add(Quiz.QuizNavKey(category, difficulty))
                },
                navigateToResult = { correctAnswers, totalAnswers ->
                    backStack.add(Quiz.ResultNavKey(correctAnswers, totalAnswers))
                },
                onBack = { backStack.removeLastOrNull() }
            )
            HistoryEntry(
                navigateToHistory = {
                    backStack.removeLastOrNull()
                    backStack.add(HistoryNavKey.ContentNavKey)
                },
                navigateToFilters = {
                    backStack.removeLastOrNull()
                    backStack.add(Quiz.FiltersNavKey)
                },
                navigateToQuizResult = { resultUiModel ->
                    backStack.removeIf { it is HistoryDetailNavKey }
                    backStack.add(HistoryDetailNavKey(Json.encodeToString(resultUiModel)))
                },
                navigateBackFromHistory = {
                    backStack.removeIf { it is HistoryNavKey.ContentNavKey || it is HistoryDetailNavKey }
                }
            )
            quizResultEntry(
                navigateToQuiz = { backStack.add(Quiz.QuizNavKey(quizId = it)) },
                navigateBack = { backStack.removeLastOrNull() })
        },
        modifier = modifier
    )
}

private fun EntryProviderScope<NavKey>.quizEntry(
    navigateBackToStart: () -> Unit,
    navigateToFilters: () -> Unit,
    navigateToHistory: () -> Unit,
    navigateToQuiz: (Category, Difficulty) -> Unit,
    navigateToResult: (correctAnswers: Int, totalAnswers: Int) -> Unit,
    onBack: () -> Unit,
) {
    entry<Quiz.StartNavKey> {
        StartScreen(
            isLoading = false,
            navigateToFilters = navigateToFilters,
            navigateToHistory = navigateToHistory
        )
    }
    entry<Quiz.FiltersNavKey> {
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
    entry<Quiz.QuizNavKey> { key ->
        QuizScreen(
            key = key,
            navigateToStart = navigateBackToStart,
            navigateToResult = navigateToResult,
            onBack = onBack
        )
    }
    entry<Quiz.ResultNavKey> {
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
private fun EntryProviderScope<NavKey>.HistoryEntry(
    navigateToHistory: () -> Unit,
    navigateToFilters: () -> Unit,
    navigateToQuizResult: (QuizResultUiModel) -> Unit,
    navigateBackFromHistory: () -> Unit
) {
    var sharedViewModelStoreOwner: ViewModelStoreOwner? by remember { mutableStateOf(null) }
    entry<HistoryNavKey.CheckerNavKey> {
        sharedViewModelStoreOwner = LocalViewModelStoreOwner.current
        HistoryCheckerScreen(
            navigateToHistory = navigateToHistory,
            navigateToFilters = navigateToFilters,
            navigateBack = navigateBackFromHistory
        )
    }
    entry<HistoryNavKey.ContentNavKey>(
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
private fun EntryProviderScope<NavKey>.quizResultEntry(
    navigateToQuiz: (quizId: Long) -> Unit,
    navigateBack: () -> Unit
) {
    entry<HistoryDetailNavKey>(
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