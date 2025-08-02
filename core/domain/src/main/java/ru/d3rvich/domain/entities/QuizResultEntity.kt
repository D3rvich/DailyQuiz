package ru.d3rvich.domain.entities

import kotlinx.datetime.LocalDateTime

data class QuizResultEntity(
    val id: Long,
    val title: String,
    val generalCategory: String,
    val passedTime: LocalDateTime,
    val questions: List<QuestionEntity>,
)

val QuizResultEntity.correctAnswers: Int
    get() {
        var result = 0
        questions.forEach {
            if (it.isCorrectAnswer) {
                result++
            }
        }
        return result
    }