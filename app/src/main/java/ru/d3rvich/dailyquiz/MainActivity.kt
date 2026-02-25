package ru.d3rvich.dailyquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import ru.d3rvich.navigation.EntryProviderInstaller
import ru.d3rvich.navigation.LocalSharedTransitionScope
import ru.d3rvich.navigation.Navigator
import ru.d3rvich.ui.theme.DailyQuizTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var entryProviderInstallers: Set<@JvmSuppressWildcards EntryProviderInstaller>

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyQuizTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val windowInfo = currentWindowAdaptiveInfo()
                    val directive = remember(windowInfo) {
                        calculatePaneScaffoldDirective(windowInfo)
                            .copy(horizontalPartitionSpacerSize = 0.dp)
                    }
                    val listDetailStrategy =
                        rememberListDetailSceneStrategy<NavKey>(directive = directive)
                    SharedTransitionLayout {
                        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                            NavDisplay(
                                backStack = navigator.backStack,
                                onBack = { navigator.goBack() },
                                sceneStrategy = listDetailStrategy,
                                entryDecorators = listOf(
                                    rememberSaveableStateHolderNavEntryDecorator(),
                                    rememberViewModelStoreNavEntryDecorator(),
                                ),
                                sharedTransitionScope = this,
                                transitionSpec = { fadeIn() togetherWith fadeOut() },
                                popTransitionSpec = { fadeIn() togetherWith fadeOut() },
                                predictivePopTransitionSpec = { fadeIn() togetherWith fadeOut() },
                                entryProvider = entryProvider {
                                    entryProviderInstallers.forEach { builder -> this.builder() }
                                })
                        }
                    }
                }
            }
        }
    }
}