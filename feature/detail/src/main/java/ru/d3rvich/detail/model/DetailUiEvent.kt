package ru.d3rvich.detail.model

import ru.d3rvich.ui.mvibase.UiEvent

internal sealed interface DetailUiEvent : UiEvent {
    data object OnBackClicked : DetailUiEvent
}