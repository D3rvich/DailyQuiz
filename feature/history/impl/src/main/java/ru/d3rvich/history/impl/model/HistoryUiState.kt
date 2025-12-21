package ru.d3rvich.history.impl.model

import androidx.compose.runtime.Immutable
import ru.d3rvich.domain.model.SortBy
import ru.d3rvich.ui.model.QuizResultUiModel
import ru.d3rvich.ui.mvibase.UiState

@Immutable
internal sealed interface HistoryUiState : UiState {
    data object Loading : HistoryUiState

    data class Content(
        val quizResultEntities: List<QuizResultUiModel>,
        val selectedSort: SortBy = SortBy.Name(true)
    ) : HistoryUiState
}