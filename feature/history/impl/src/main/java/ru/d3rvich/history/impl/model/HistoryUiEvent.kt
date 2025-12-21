package ru.d3rvich.history.impl.model

import ru.d3rvich.domain.model.SortBy
import ru.d3rvich.ui.model.QuizResultUiModel
import ru.d3rvich.ui.mvibase.UiEvent

internal sealed interface HistoryUiEvent : UiEvent {

    data class OnRemoveQuiz(val quiz: QuizResultUiModel) : HistoryUiEvent

    data class OnSortChange(val selectedSort: SortBy) : HistoryUiEvent
}