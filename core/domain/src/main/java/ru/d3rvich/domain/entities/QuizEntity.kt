package ru.d3rvich.domain.entities

data class QuizEntity(
    val generalCategory: String,
    val questions: List<QuestionEntity>
)

inline val QuizEntity.correctAnswers: Int
    get() {
        var result = 0
        questions.forEach { if (it.isCorrectAnswer) result++ }
        return result
    }