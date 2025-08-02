package ru.d3rvich.database.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDBO(
    @SerialName("category") val category: String,
    @SerialName("text") val text: String,
    @SerialName("answers") val answers: List<AnswerDBO>,
    @SerialName("selected_answer_index") val selectedAnswerIndex: Int,
)