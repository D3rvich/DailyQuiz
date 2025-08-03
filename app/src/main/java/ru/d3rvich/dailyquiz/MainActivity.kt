package ru.d3rvich.dailyquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.d3rvich.result.QuizResultScreen
import ru.d3rvich.history.HistoryScreen
import ru.d3rvich.quiz.QuizScreen
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
                NavHost(navController, startDestination = Screens.Quiz()) {
                    composable<Screens.Quiz> {
                        QuizScreen(navigateToHistory = { navController.navigate(Screens.History) })
                    }
                    composable<Screens.History> {
                        HistoryScreen(
                            navigateToQuizResult = {
                                navController.navigate(Screens.QuizResult(it))
                            },
                            navigateToQuiz = {
                                navController.navigate(Screens.Quiz(startNewQuiz = true)) {
                                    popUpTo<Screens.Quiz>()
                                }
                            }
                        )
                    }
                    composable<Screens.QuizResult> {
                        QuizResultScreen({
                            navController.navigate(
                                route = Screens.Quiz(
                                    startNewQuiz = true,
                                    quizId = it
                                )
                            ) {
                                popUpTo<Screens.Quiz>()
                            }
                        })
                    }
                }
            }
        }
    }
}