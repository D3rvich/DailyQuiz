package ru.d3rvich.history.model

import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.ui.mvibase.UiEvent

internal sealed interface HistoryUiEvent : UiEvent {

    data class OnRemoveQuiz(val quiz: QuizResultEntity) : HistoryUiEvent
}