package ru.d3rvich.ui.navigation

import kotlinx.serialization.Serializable

object Screens {

    @Serializable
    data object Quiz

    @Serializable
    data object History

    @Serializable
    data class QuizDebriefing(val quizId: Long)
}