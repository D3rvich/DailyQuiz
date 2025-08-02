package ru.d3rvich.domain.entities

import kotlinx.datetime.LocalDateTime

data class QuizResultEntity(
    val id: Long,
    val title: String,
    val generalCategory: String,
    val passedTime: LocalDateTime,
    val questions: List<QuestionEntity>,
)
