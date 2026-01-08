package ru.d3rvich.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty

@Immutable
data class QuizUiModel(
    val category: Category,
    val difficulty: Difficulty,
    val questions: ImmutableList<QuestionUiModel>
)

inline val QuizUiModel.correctAnswers: Int
    get() = questions.filter { it.isCorrectAnswer }.size