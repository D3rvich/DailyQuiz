package ru.d3rvich.result.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.d3rvich.navigation.Navigator
import ru.d3rvich.ui.model.QuizResultUiModel

@Serializable
data class HistoryDetailNavKey(val quizJson: String) : NavKey

fun Navigator.navigateToHistoryDetail(quiz: QuizResultUiModel) {
    val quizJson = Json.encodeToString(quiz)
    navigate(HistoryDetailNavKey(quizJson))
}