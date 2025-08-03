package ru.d3rvich.ui.navigation

import kotlinx.serialization.Serializable

object Screens {

    @Serializable
    data class Quiz(val repeatQuizId: Long? = null)

    @Serializable
    data object History

    @Serializable
    data class QuizResult(val quizId: Long)
}