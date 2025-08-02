package ru.d3rvich.history.model

import androidx.compose.runtime.Immutable
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.ui.mvibase.UiState

@Immutable
internal sealed interface HistoryUiState : UiState {
    data object Idle : HistoryUiState

    data class Content(val quizResultEntities: List<QuizResultEntity>) : HistoryUiState
}