package ru.d3rvich.history.impl.screens.history.model

import ru.d3rvich.ui.model.QuizResultUiModel
import ru.d3rvich.ui.model.SortByUiModel
import ru.d3rvich.ui.mvibase.UiEvent

internal sealed interface HistoryUiEvent : UiEvent {

    data class OnRemoveQuiz(val quiz: QuizResultUiModel) : HistoryUiEvent

    data class OnSortChange(val selectedSort: SortByUiModel) : HistoryUiEvent
}