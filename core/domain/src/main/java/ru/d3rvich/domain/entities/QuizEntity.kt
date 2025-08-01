package ru.d3rvich.domain.entities

data class QuizEntity(
    val generalCategory: String,
    val questions: List<QuestionEntity>
)
