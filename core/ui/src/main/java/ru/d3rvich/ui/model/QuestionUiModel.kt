package ru.d3rvich.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable
import ru.d3rvich.ui.utils.ImmutableListSerializer

@Immutable
@Serializable
data class QuestionUiModel(
    val category: String,
    val text: String,
    @Serializable(with = ImmutableListSerializer::class)
    val answers: ImmutableList<AnswerUiModel>,
    val selectedAnswerIndex: Int? = null,
)

val QuestionUiModel.isCorrectAnswer: Boolean
    get() = selectedAnswerIndex?.let { selectedAnswerIndexNotNull ->
        answers[selectedAnswerIndexNotNull].isCorrect
    } ?: false