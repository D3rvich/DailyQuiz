package ru.d3rvich.detail.model

import ru.d3rvich.ui.mvibase.UiAction

internal sealed interface DetailUiAction : UiAction {
    data object NavigateBack : DetailUiAction
}