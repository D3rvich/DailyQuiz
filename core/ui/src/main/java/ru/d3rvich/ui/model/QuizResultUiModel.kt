package ru.d3rvich.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult

@Immutable
@Serializable
data class QuizResultUiModel(
    val generalCategory: Category,
    val difficult: Difficult,
    val passedTime: LocalDateTime,
    val questions: List<QuestionUiModel>,
    val id: Long = 0,
)

val QuizResultUiModel.correctAnswers: Int
    get() {
        var result = 0
        questions.forEach {
            if (it.isCorrectAnswer) {
                result++
            }
        }
        return result
    }