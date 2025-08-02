package ru.d3rvich.debriefing.model

import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.ui.mvibase.UiState

internal sealed interface DebriefingUiState : UiState {

    data object Idle : DebriefingUiState

    data class Content(val quiz: QuizResultEntity) : DebriefingUiState
}