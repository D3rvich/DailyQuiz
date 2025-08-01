package ru.d3rvich.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
internal data class Quiz(@SerialName("results") val questions: List<Question>)

@OptIn(InternalSerializationApi::class)
@Serializable
data class Question(
    @SerialName("difficulty") val difficulty: String,
    @SerialName("category") val category: String,
    @SerialName("question") val question: String,
    @SerialName("correct_answer") val currentAnswer: String,
    @SerialName("incorrect_answers") val incorrectAnswers: List<String>
)
