package ru.d3rvich.list.model

import ru.d3rvich.ui.mvibase.UiAction

internal sealed interface ListUiAction : UiAction {
    data class NavigateToDetail(val itemId: Int) : ListUiAction
}