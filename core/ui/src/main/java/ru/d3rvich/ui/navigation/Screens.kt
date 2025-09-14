package ru.d3rvich.ui.navigation

import kotlinx.serialization.Serializable
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty

object Screens {

    @Serializable
    data object QuizMain {

        @Serializable
        data object Start

        @Serializable
        data object Filters

        @Serializable
        data class Quiz(
            val category: Category = Category.AnyCategory,
            val difficulty: Difficulty = Difficulty.AnyDifficulty,
            val quizId: Long? = null
        )

        @Serializable
        data class Result(val correctAnswers: Int, val totalAnswers: Int)
    }

    @Serializable
    data object History

    @Serializable
    data class QuizResult(val quizResultJson: String)
}