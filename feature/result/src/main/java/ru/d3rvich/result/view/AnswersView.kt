package ru.d3rvich.result.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.d3rvich.result.R
import ru.d3rvich.ui.components.CorrectCheckIcon
import ru.d3rvich.ui.model.AnswerUiModel
import ru.d3rvich.ui.theme.DailyQuizTheme
import ru.d3rvich.ui.R as uiR

@Composable
internal fun AnswersView(
    answers: List<AnswerUiModel>,
    selectedAnswerIndex: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        answers.forEachIndexed { index, answer ->
            Answer(
                text = answer.text,
                isSelected = index == selectedAnswerIndex,
                isCorrectAnswer = answer.isCorrect
            )
        }
    }
}

@Composable
private fun Answer(
    text: String,
    isSelected: Boolean,
    isCorrectAnswer: Boolean,
    modifier: Modifier = Modifier
) {
    val borderColor = remember {
        when {
            !isSelected -> Color.Transparent
            isSelected && isCorrectAnswer -> Color(0xFF00AE3A)
            else -> Color(0xFFE70000)
        }
    }
    val containerColor =
        if (isSelected) CardDefaults.cardColors().containerColor else Color(0xFFF3F3F3)
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(2.dp, color = borderColor),
        colors = CardDefaults.cardColors().copy(containerColor = containerColor)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.padding(12.dp)) {
                if (!isSelected) {
                    Icon(
                        painterResource(uiR.drawable.ic_outline_circle),
                        contentDescription = stringResource(R.string.unselected_answer)
                    )
                } else {
                    CorrectCheckIcon(isCorrectAnswer)
                }
            }
            Text(text = AnnotatedString.fromHtml(text))
        }
    }
}

@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun AnswerUnselectedPreview() {
    DailyQuizTheme {
        Answer(
            text = "Answer text",
            isSelected = false,
            isCorrectAnswer = true,
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun AnswerCorrectPreview() {
    DailyQuizTheme {
        Answer(
            text = "Answer text",
            isSelected = true,
            isCorrectAnswer = true,
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun AnswerWrongPreview() {
    DailyQuizTheme {
        Answer(
            text = "Answer text",
            isSelected = true,
            isCorrectAnswer = false,
            modifier = Modifier.padding(20.dp)
        )
    }
}