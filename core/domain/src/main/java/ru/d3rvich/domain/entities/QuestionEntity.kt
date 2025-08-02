package ru.d3rvich.domain.entities

data class QuestionEntity(
    val id: String,
    val category: String,
    val text: String,
    val answers: List<AnswerEntity>,
    val selectedAnswerIndex: Int? = null,
)