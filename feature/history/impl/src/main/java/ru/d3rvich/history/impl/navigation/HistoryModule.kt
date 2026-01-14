package ru.d3rvich.history.impl.navigation

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation3.ui.NavDisplay
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import ru.d3rvich.history.api.navigation.History
import ru.d3rvich.history.api.navigation.navigateToEmptyHistory
import ru.d3rvich.history.impl.R
import ru.d3rvich.history.impl.screens.EmptyHistoryScreen
import ru.d3rvich.history.impl.screens.history.HistoryScreen
import ru.d3rvich.navigation.EntryProviderInstaller
import ru.d3rvich.navigation.Navigator
import ru.d3rvich.quiz.api.navigation.Quiz
import ru.d3rvich.result.api.navigation.HistoryDetailNavKey
import ru.d3rvich.result.api.navigation.navigateToHistoryDetail

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object HistoryModule {

    @Provides
    @IntoSet
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        val popTransitionSpec = NavDisplay.popTransitionSpec {
            fadeIn() + scaleIn(initialScale = 0.9f) togetherWith slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(500)
            )
        }
        val predictivePopTransitionSpec = NavDisplay.predictivePopTransitionSpec {
            fadeIn() + scaleIn(initialScale = 0.9f) togetherWith slideOutVertically(
                targetOffsetY = { it }, animationSpec = tween(500)
            )
        }
        entry<History.HistoryNavKey>(
            metadata = ListDetailSceneStrategy.listPane {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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
            } + NavDisplay.transitionSpec {
                slideInVertically(initialOffsetY = { it }, animationSpec = tween(500)) togetherWith
                        ExitTransition.KeepUntilTransitionsFinished
            } + popTransitionSpec + predictivePopTransitionSpec
        ) {
            HistoryScreen(
                navigateToEmptyHistory = {
                    navigator.backStack.remove(History.HistoryNavKey)
                    navigator.navigateToEmptyHistory()
                },
                navigateToQuizResult = { quizId ->
                    navigator.navigateToHistoryDetail(quizId = quizId)
                },
                navigateBack = {
                    navigator.backStack.removeIf { it is History.HistoryNavKey || it is HistoryDetailNavKey }
                },
            )
        }
        entry<History.EmptyHistoryNavKey>(
            metadata = NavDisplay.transitionSpec { fadeIn() togetherWith fadeOut() } +
                    popTransitionSpec + predictivePopTransitionSpec
        ) {
            EmptyHistoryScreen(
                onStartQuizClick = {
                    navigator.backStack.remove(History.EmptyHistoryNavKey)
                    navigator.navigate(Quiz.FiltersNavKey)
                },
                onBackClick = { navigator.backStack.remove(History.EmptyHistoryNavKey) }
            )
        }
    }
}