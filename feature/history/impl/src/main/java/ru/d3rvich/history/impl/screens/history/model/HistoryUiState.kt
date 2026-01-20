package ru.d3rvich.history.impl.screens.history.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.d3rvich.ui.model.QuizResultUiModel
import ru.d3rvich.ui.model.SortByUiModel
import ru.d3rvich.ui.mvibase.UiState

@Immutable
internal sealed class HistoryUiState(
    open val selectedSort: SortByUiModel = SortByUiModel.Name(true)
) : UiState {
    data object Loading : HistoryUiState()

    data class Content(
        val quizResultEntities: ImmutableList<QuizResultUiModel>,
        override val selectedSort: SortByUiModel,
    ) : HistoryUiState()
}