package ru.d3rvich.quiz.model

import ru.d3rvich.ui.mvibase.UiAction

sealed interface QuizUiAction : UiAction {
    data object ShowError : QuizUiAction
    data object NavigateToStart : QuizUiAction
    data class NavigateToResults(val correctAnswers: Int, val totalAnswers: Int) : QuizUiAction
}