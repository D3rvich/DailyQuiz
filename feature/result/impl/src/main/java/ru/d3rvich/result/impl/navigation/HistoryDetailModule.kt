package ru.d3rvich.result.impl.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import ru.d3rvich.navigation.EntryProviderInstaller
import ru.d3rvich.navigation.LocalSharedTransitionScope
import ru.d3rvich.navigation.Navigator
import ru.d3rvich.quiz.api.navigation.navigateToQuiz
import ru.d3rvich.result.api.navigation.HistoryDetailNavKey
import ru.d3rvich.result.impl.HistoryDetailScreen

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object HistoryDetailModule {

    @Provides
    @IntoSet
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<HistoryDetailNavKey>(
            metadata = ListDetailSceneStrategy.detailPane()
        ) { key ->
            with(LocalSharedTransitionScope.current) {
                HistoryDetailScreen(
                    modifier = Modifier.sharedBounds(
                        rememberSharedContentState(key = "quiz:${key.quizId}"),
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current
                    ),
                    quizId = key.quizId,
                    navigateToQuiz = { quizId -> navigator.navigateToQuiz(quizId = quizId) },
                    navigateBack = { navigator.backStack.removeIf { it is HistoryDetailNavKey } }
                )
            }
        }
    }
}
