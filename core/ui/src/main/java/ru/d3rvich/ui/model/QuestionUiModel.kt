package ru.d3rvich.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class QuestionUiModel(
    val category: String,
    val text: String,
    val answers: ImmutableList<AnswerUiModel>,
    val selectedAnswerIndex: Int? = null,
)

val QuestionUiModel.isCorrectAnswer: Boolean
    get() = selectedAnswerIndex?.let { selectedAnswerIndexNotNull ->
        answers[selectedAnswerIndexNotNull].isCorrect
    } ?: false