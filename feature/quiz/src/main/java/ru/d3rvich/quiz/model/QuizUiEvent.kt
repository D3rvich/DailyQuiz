package ru.d3rvich.quiz.model

import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.ui.mvibase.UiEvent

internal sealed interface QuizUiEvent : UiEvent {
    data object OnStartClicked : QuizUiEvent
    data object OnRetryClicked : QuizUiEvent
    data object OnNextClicked : QuizUiEvent
    data object OnBackClicked : QuizUiEvent
    data object EnterScreen : QuizUiEvent
    data class OnAnswerSelected(val index: Int) : QuizUiEvent
    data class OnCategorySelected(val category: Category) : QuizUiEvent
    data class OnDifficultSelected(val difficult: Difficult) : QuizUiEvent
}