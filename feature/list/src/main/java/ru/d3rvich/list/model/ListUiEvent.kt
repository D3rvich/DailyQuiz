package ru.d3rvich.list.model

import ru.d3rvich.ui.mvibase.UiEvent

internal sealed interface ListUiEvent: UiEvent {
    data class OnItemSelected(val itemId: Int) : ListUiEvent
}