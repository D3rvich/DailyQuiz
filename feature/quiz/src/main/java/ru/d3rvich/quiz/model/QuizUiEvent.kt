package ru.d3rvich.quiz.model

import ru.d3rvich.ui.mvibase.UiEvent

sealed interface QuizUiEvent : UiEvent {
    data object OnNextClicked : QuizUiEvent
    data object OnRetryClicked : QuizUiEvent
    data class OnAnswerSelected(val index: Int) : QuizUiEvent
}