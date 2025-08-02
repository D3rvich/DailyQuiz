package ru.d3rvich.domain.entities

import kotlinx.datetime.LocalDateTime

data class QuizResultEntity(
    val id: String,
    val generalCategory: String,
    val questions: List<QuestionEntity>,
    val selectedAnswerId: String,
    val passedTime: LocalDateTime,
)
