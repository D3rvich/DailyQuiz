package ru.d3rvich.quiz.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty
import ru.d3rvich.navigation.Navigator

object Quiz {

    @Serializable
    data object StartNavKey : NavKey

    @Serializable
    data object FiltersNavKey : NavKey

    @Serializable
    data class QuizNavKey(
        val category: Category = Category.AnyCategory,
        val difficulty: Difficulty = Difficulty.AnyDifficulty,
        val quizId: Long? = null
    ) : NavKey

    @Serializable
    data class Result(val correctAnswers: Int, val totalAnswers: Int) : NavKey
}

fun Navigator.navigateToFilters() {
    navigate(Quiz.FiltersNavKey)
}

fun Navigator.navigateToQuiz(
    category: Category = Category.AnyCategory,
    difficulty: Difficulty = Difficulty.AnyDifficulty,
    quizId: Long? = null
) {
    navigate(Quiz.QuizNavKey(category, difficulty, quizId))
}

fun Navigator.navigateToResult(correctAnswers: Int, totalAnswers: Int) {
    navigate(Quiz.Result(correctAnswers, totalAnswers))
}