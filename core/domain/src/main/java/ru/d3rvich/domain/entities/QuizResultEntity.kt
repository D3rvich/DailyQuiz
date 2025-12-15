package ru.d3rvich.domain.entities

import kotlinx.datetime.LocalDateTime
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty

data class QuizResultEntity(
    val generalCategory: Category,
    val difficulty: Difficulty,
    val passedTime: LocalDateTime,
    val questions: List<QuestionEntity>,
    val correctAnswers: Int,
    val id: Long = 0,
)