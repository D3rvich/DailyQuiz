package ru.d3rvich.result.impl.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ru.d3rvich.ui.components.AnswerType
import ru.d3rvich.ui.components.AnswerUiCard
import ru.d3rvich.ui.model.AnswerUiModel
import ru.d3rvich.ui.theme.DailyQuizTheme

@Composable
internal fun AnswersView(
    answers: ImmutableList<AnswerUiModel>,
    selectedAnswerIndex: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        answers.forEachIndexed { index, answer ->
            val isSelected = index == selectedAnswerIndex
            val type = when {
                isSelected && answer.isCorrect -> AnswerType.Correct
                isSelected && !answer.isCorrect -> AnswerType.Wrong
                else -> AnswerType.NotSelected
            }
            AnswerUiCard(answerType = type, text = answer.text)
        }
    }
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun QuestionCardPreview() {
    DailyQuizTheme {
        val answers = List(4) {
            AnswerUiModel("Answer $it", it == 1)
        }
        Surface(color = MaterialTheme.colorScheme.background) {
            AnswersView(
                answers = answers.toPersistentList(),
                selectedAnswerIndex = 1
            )
        }
    }
}