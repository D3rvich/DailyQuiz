package ru.d3rvich.quiz

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.quiz.views.FiltersView
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
    composable<Screens.QuizMain.Start>(
        exitTransition = {
            targetState.destination.route?.let { route ->
                when (route) {
                    Screens.QuizMain.Filters::class.qualifiedName ->
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)

                    else -> null
                }
            }
        },
        enterTransition = {
            initialState.destination.route?.let { route ->
                when (route) {
                    Screens.QuizMain.Filters::class.qualifiedName -> slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right
                    )

                    else -> null
                }
            }
        }
    ) {
        StartView(
            isLoading = false,
            onStartClick = { navController.navigate(Screens.QuizMain.Filters) },
            onHistoryClick = { navController.navigate(Screens.History) }
        )
    }
}

private fun NavGraphBuilder.filtersScreen(navController: NavController) {
    composable<Screens.QuizMain.Filters>(
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
        },
        exitTransition = {
            targetState.destination.route?.let {
                when (route) {
                    Screens.QuizMain.Start::class.qualifiedName, Screens.QuizMain::class.qualifiedName -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right
                    )

                    Screens.QuizMain.Quiz::class.qualifiedName -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left
                    )

                    else -> null
                }
            }
        }) {
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
            },
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}

private fun NavGraphBuilder.quiz(navController: NavController) {
    composable<Screens.QuizMain.Quiz>(
        enterTransition = {
            initialState.destination.route?.let { route ->
                when (route) {
                    Screens.QuizMain.Filters::class.qualifiedName -> slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left
                    )

                    else -> null
                }
            }
        },
        exitTransition = {
            targetState.destination.route?.let { route ->
                when (route) {
                    Screens.QuizMain.Start::class.qualifiedName, Screens.QuizMain::class.qualifiedName ->
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)

                    Screens.QuizMain.Result::class.qualifiedName -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left
                    )

                    else -> null
                }
            }
        }
    ) {
        QuizScreen(
            navigateToStart = {
                navController.navigate(Screens.QuizMain.Start) {
                    popUpTo<Screens.QuizMain.Start>()
                }
            },
            navigateToResult = { correctAnswers, totalAnswers ->
                navController.navigate(
                    Screens.QuizMain.Result(
                        correctAnswers,
                        totalAnswers
                    )
                ) {
                    popUpTo<Screens.QuizMain.Start>()
                }
            },
            onBackClick = { navController.popBackStack() }
        )
    }
}

private fun NavGraphBuilder.result(navController: NavController) {
    composable<Screens.QuizMain.Result>(
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
        exitTransition = {
            targetState.destination.route?.let {
                when (route) {
                    Screens.QuizMain.Start::class.qualifiedName, Screens.QuizMain::class.qualifiedName ->
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)

                    else -> null
                }
            }
        }) {
        val args = it.toRoute<Screens.QuizMain.Result>()
        ResultsView(args.correctAnswers, args.totalAnswers) {
            navController.navigate(Screens.QuizMain.Start) {
                popUpTo<Screens.QuizMain.Start>()
            }
        }
    }
}