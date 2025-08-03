package ru.d3rvich.domain.entities

import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult

data class QuizEntity(
    val generalCategory: Category,
    val difficult: Difficult,
    val questions: List<QuestionEntity>
)

inline val QuizEntity.correctAnswers: Int
    get() {
        var result = 0
        questions.forEach { if (it.isCorrectAnswer) result++ }
        return result
    }