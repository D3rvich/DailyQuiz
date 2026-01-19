package ru.d3rvich.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import ru.d3rvich.ui.theme.DailyQuizTheme
import ru.d3rvich.ui.theme.Success
import ru.d3rvich.ui.theme.Error

@Composable
fun AnswerUiCard(text: String, answerType: AnswerType, modifier: Modifier = Modifier) {
    val containerColor = when (answerType) {
        AnswerType.NotSelected -> MaterialTheme.colorScheme.surfaceContainerHigh
        else -> Color.Transparent
    }
    val borderColor by animateColorAsState(
        when (answerType) {
            AnswerType.NotSelected -> Color.Transparent
            AnswerType.Selected -> MaterialTheme.colorScheme.primary
            AnswerType.Correct -> Success
            AnswerType.Wrong -> Error
        }
    )
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(2.dp, color = borderColor),
        colors = CardDefaults.cardColors().copy(containerColor = containerColor)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedContent(
                answerType,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                modifier = Modifier.padding(16.dp)
            ) { type ->
                when (type) {
                    AnswerType.NotSelected -> DailyQuizRadioButtonIcon(false)
                    AnswerType.Selected -> DailyQuizRadioButtonIcon(true)
                    AnswerType.Correct -> CorrectCheckIcon(true)
                    AnswerType.Wrong -> CorrectCheckIcon(false)
                }
            }
            Text(text = AnnotatedString.fromHtml(text), modifier = Modifier.padding(end = 4.dp))
        }
    }
}

enum class AnswerType {
    NotSelected,
    Selected,
    Correct,
    Wrong,
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun AnswerUiCardsPreview() {
    DailyQuizTheme {
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AnswerType.entries.forEach {
                AnswerUiCard(text = it.name, answerType = it)
            }
        }
    }
}