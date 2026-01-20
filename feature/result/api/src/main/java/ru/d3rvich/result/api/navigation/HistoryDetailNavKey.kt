package ru.d3rvich.result.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.d3rvich.navigation.Navigator

@Serializable
data class HistoryDetailNavKey(val quizId: Long) : NavKey

fun Navigator.navigateToHistoryDetail(quizId: Long) {
    navigate(HistoryDetailNavKey(quizId))
}