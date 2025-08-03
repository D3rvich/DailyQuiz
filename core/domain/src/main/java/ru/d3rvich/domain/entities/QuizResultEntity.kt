package ru.d3rvich.domain.entities

import kotlinx.datetime.LocalDateTime
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult

data class QuizResultEntity(
    val generalCategory: Category,
    val difficult: Difficult,
    val passedTime: LocalDateTime,
    val questions: List<QuestionEntity>,
    val id: Long = 0,
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