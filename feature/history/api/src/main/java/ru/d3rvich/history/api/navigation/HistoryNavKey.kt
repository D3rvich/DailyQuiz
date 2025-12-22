package ru.d3rvich.history.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.d3rvich.navigation.Navigator

@Serializable
data object HistoryNavKey : NavKey {

    @Serializable
    data object CheckerNavKey : NavKey

    @Serializable
    data object ContentNavKey : NavKey
}

fun Navigator.navigateToHistory() {
    navigate(HistoryNavKey)
}