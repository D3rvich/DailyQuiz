package ru.d3rvich.quiz.model

import ru.d3rvich.ui.mvibase.UiEvent

internal sealed interface QuizUiEvent : UiEvent {
    data object OnStartClicked : QuizUiEvent
    data object OnRetryClicked : QuizUiEvent
    data object OnNextClicked : QuizUiEvent
    data object OnBackClicked : QuizUiEvent
    data class OnAnswerSelected(val index: Int) : QuizUiEvent
}