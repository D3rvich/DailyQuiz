package ru.d3rvich.quiz.impl.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty
import ru.d3rvich.history.api.navigation.navigateToHistoryContent
import ru.d3rvich.navigation.EntryProviderInstaller
import ru.d3rvich.navigation.Navigator
import ru.d3rvich.quiz.api.navigation.Quiz
import ru.d3rvich.quiz.api.navigation.navigateToFilters
import ru.d3rvich.quiz.api.navigation.navigateToQuiz
import ru.d3rvich.quiz.api.navigation.navigateToResult
import ru.d3rvich.quiz.api.navigation.navigateToStart
import ru.d3rvich.quiz.impl.screens.FiltersScreen
import ru.d3rvich.quiz.impl.screens.QuizScreen
import ru.d3rvich.quiz.impl.screens.ResultsScreen
import ru.d3rvich.quiz.impl.screens.StartScreen

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object QuizModule {

    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<Quiz.StartNavKey> {
            StartScreen(
                isLoading = false,
                navigateToFilters = { navigator.navigateToFilters() },
                navigateToHistory = { navigator.navigateToHistoryContent() }
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
                onStartQuiz = { navigator.navigateToQuiz(category!!, difficulty!!) },
                onBack = { navigator.backStack.remove(Quiz.FiltersNavKey) }
            )
        }
        entry<Quiz.QuizNavKey> { key ->
            QuizScreen(
                key = key,
                navigateToStart = {
                    navigator.clear()
                    navigator.navigateToStart()
                },
                navigateToResult = { correctAnswers, totalAnswers ->
                    navigator.backStack.removeIf { it is Quiz.FiltersNavKey || it is Quiz.QuizNavKey }
                    navigator.navigateToResult(correctAnswers, totalAnswers)
                },
                onBack = { navigator.backStack.removeIf { it is Quiz.QuizNavKey } }
            )
        }
        entry<Quiz.ResultNavKey> { key ->
            val (correctAnswers, totalAnswers) = key
            ResultsScreen(
                correctAnswers = correctAnswers,
                totalQuestions = totalAnswers,
                navigateToSource = {
                    navigator.backStack.removeIf { it is Quiz.ResultNavKey }
                }
            )
        }
    }
}