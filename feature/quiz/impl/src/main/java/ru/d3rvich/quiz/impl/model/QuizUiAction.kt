package ru.d3rvich.quiz.impl.model

import ru.d3rvich.ui.mvibase.UiAction

internal sealed interface QuizUiAction : UiAction {
    data object ShowError : QuizUiAction
    data object NavigateToStart : QuizUiAction
    data class NavigateToResults(val correctAnswers: Int, val totalAnswers: Int) : QuizUiAction
}