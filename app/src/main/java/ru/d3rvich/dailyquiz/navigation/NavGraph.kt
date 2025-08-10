package ru.d3rvich.dailyquiz.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.json.Json
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.history.HistoryScreen
import ru.d3rvich.quiz.quizRoute
import ru.d3rvich.result.QuizResultScreen
import ru.d3rvich.ui.model.QuizResultUiModel
import ru.d3rvich.ui.navigation.Screens

@Composable
internal fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screens.QuizMain, modifier = modifier) {
        quizRoute(navController)
        history(navController)
        quizResult(navController)
    }
}

private fun NavGraphBuilder.history(navController: NavController) {
    composable<Screens.History>(
        enterTransition = {
            initialState.destination.route?.let { route ->
                when (route) {
                    Screens.QuizMain.Start::class.qualifiedName, Screens.QuizMain::class.qualifiedName ->
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)

                    else -> null
                }
            }
        },
        exitTransition = {
            targetState.destination.route?.let { route ->
                when (route) {
                    Screens.QuizMain.Start::class.qualifiedName, Screens.QuizMain::class.qualifiedName ->
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)

                    else -> null
                }
            }
        }) {
        HistoryScreen(
            navigateToQuizResult = {
                navController.navigate(Screens.QuizResult(Json.encodeToString(it)))
            },
            navigateToQuiz = {
                navController.navigate(
                    Screens.QuizMain.Quiz(
                        Category.AnyCategory,
                        Difficult.AnyDifficulty
                    )
                ) {
                    launchSingleTop = true
                    popUpTo<Screens.QuizMain.Start>()
                }
            }
        )
    }
}

private fun NavGraphBuilder.quizResult(navController: NavController) {
    composable<Screens.QuizResult> { backStackEntry ->
        val quizResultJson =
            backStackEntry.toRoute<Screens.QuizResult>().quizResultJson
        val quizResult = Json.decodeFromString<QuizResultUiModel>(quizResultJson)
        QuizResultScreen(
            quizResult,
            navigateToQuiz = {
                navController.navigate(Screens.QuizMain.Quiz(quizId = it)) {
                    launchSingleTop = true
                }
            },
        )
    }
}