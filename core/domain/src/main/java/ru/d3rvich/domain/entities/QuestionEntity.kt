package ru.d3rvich.domain.entities

data class QuestionEntity(
    val category: String,
    val text: String,
    val answers: List<AnswerEntity>,
    val selectedAnswerIndex: Int? = null,
)

val QuestionEntity.isCorrectAnswer: Boolean
    get() = selectedAnswerIndex?.let { selectedAnswerIndexNotNull ->
        answers[selectedAnswerIndexNotNull].isCorrect
    } ?: false