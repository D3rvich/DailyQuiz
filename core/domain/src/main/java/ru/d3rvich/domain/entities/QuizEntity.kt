package ru.d3rvich.domain.entities

data class QuizEntity(
    val id: String,
    val generalCategory: String,
    val questions: List<QuestionEntity>
)
