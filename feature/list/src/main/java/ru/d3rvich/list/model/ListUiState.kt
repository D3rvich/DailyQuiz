package ru.d3rvich.list.model

import androidx.compose.runtime.Immutable
import ru.d3rvich.domain.entities.QuestionEntity
import ru.d3rvich.ui.mvibase.UiState

@Immutable
internal sealed interface ListUiState : UiState {
    data object Loading : ListUiState
    data class Content(val data: List<QuestionEntity>) : ListUiState
}