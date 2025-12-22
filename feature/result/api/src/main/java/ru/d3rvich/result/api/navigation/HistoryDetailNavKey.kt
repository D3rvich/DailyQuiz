package ru.d3rvich.result.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.navigation.Navigator

@Serializable
data class HistoryDetailNavKey(val quizResultJson: String) : NavKey

fun Navigator.navigateToResult(quizResult: QuizResultEntity) {
    navigate(HistoryDetailNavKey(Json.encodeToString(quizResult)))
}