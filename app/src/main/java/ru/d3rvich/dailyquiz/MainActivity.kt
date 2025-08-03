package ru.d3rvich.dailyquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.result.QuizResultScreen
import ru.d3rvich.history.HistoryScreen
import ru.d3rvich.quiz.quizRoute
import ru.d3rvich.ui.navigation.Screens
import ru.d3rvich.ui.theme.DailyQuizTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyQuizTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = Screens.QuizMain) {
                    quizRoute(navController)
                    composable<Screens.History> {
                        HistoryScreen(
                            navigateToQuizResult = {
                                navController.navigate(Screens.QuizResult(it))
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
                    composable<Screens.QuizResult> {
                        QuizResultScreen({
                            navController.navigate(
                                Screens.QuizMain.Quiz(quizId = it)
                            ) {
                                launchSingleTop = true
                                popUpTo<Screens.QuizMain.Start>()
                            }
                        })
                    }
                }
            }
        }
    }
}