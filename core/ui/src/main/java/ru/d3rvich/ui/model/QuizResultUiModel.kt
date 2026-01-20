package ru.d3rvich.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDateTime
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty

@Immutable
data class QuizResultUiModel(
    val generalCategory: Category,
    val difficulty: Difficulty,
    val passedTime: LocalDateTime,
    val questions: ImmutableList<QuestionUiModel>,
    val correctAnswers: Int,
    val id: Long = 0,
)