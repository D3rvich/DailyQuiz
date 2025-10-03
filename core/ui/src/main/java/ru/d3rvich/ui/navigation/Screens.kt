package ru.d3rvich.ui.navigation

import kotlinx.serialization.Serializable
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty

interface Screen

object Screens {

    object QuizMain {

        @Serializable
        data object Start: Screen

        @Serializable
        data object Filters: Screen

        @Serializable
        data class Quiz(
            val category: Category = Category.AnyCategory,
            val difficulty: Difficulty = Difficulty.AnyDifficulty,
            val quizId: Long? = null
        ): Screen

        @Serializable
        data class Result(val correctAnswers: Int, val totalAnswers: Int): Screen
    }

    @Serializable
    data object History: Screen

    @Serializable
    data class QuizResult(val quizResultJson: String): Screen
}