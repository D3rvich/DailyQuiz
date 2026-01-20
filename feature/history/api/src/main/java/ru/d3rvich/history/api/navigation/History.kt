package ru.d3rvich.history.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.d3rvich.navigation.Navigator

data object History {

    @Serializable
    data object EmptyHistoryNavKey : NavKey

    @Serializable
    data object HistoryNavKey : NavKey
}

fun Navigator.navigateToEmptyHistory() {
    navigate(History.EmptyHistoryNavKey)
}

fun Navigator.navigateToHistoryContent() {
    navigate(History.HistoryNavKey)
}