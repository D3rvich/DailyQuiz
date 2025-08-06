package ru.d3rvich.ui.model

import androidx.compose.runtime.Immutable
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult

@Immutable
data class QuizUiModel(
    val category: Category,
    val difficult: Difficult,
    val questions: List<QuestionUiModel>
)

inline val QuizUiModel.correctAnswers: Int
    get() {
        var result = 0
        questions.forEach { if (it.isCorrectAnswer) result++ }
        return result
    }