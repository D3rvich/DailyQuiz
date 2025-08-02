package ru.d3rvich.database.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnswerDBO(
    @SerialName("text") val text: String,
    @SerialName("is_correct") val isCorrect: Boolean
)