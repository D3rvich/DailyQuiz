package ru.d3rvich.domain.entities

data class QuestionEntity(
    val id: Int,
    val category: String,
    val question: String,
    val currentAnswer: Answer,
    val wrongAnswers: List<Answer>,
)

data class Answer(val id: String, val text: String)