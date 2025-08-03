package ru.d3rvich.result.model

import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.ui.mvibase.UiState

internal sealed interface QuizResultUiState : UiState {

    data object Loading : QuizResultUiState

    data class Content(val quiz: QuizResultEntity) : QuizResultUiState
}