package ru.d3rvich.detail.model

import androidx.compose.runtime.Immutable
import ru.d3rvich.domain.entities.SampleEntity
import ru.d3rvich.ui.mvibase.UiState

@Immutable
internal sealed interface DetailUiState : UiState {
    data object Loading : DetailUiState
    data class Detail(val entity: SampleEntity) : DetailUiState
}