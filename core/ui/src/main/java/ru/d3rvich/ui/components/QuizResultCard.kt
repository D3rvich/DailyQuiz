package ru.d3rvich.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.d3rvich.ui.R
import ru.d3rvich.ui.model.ResultMessage
import ru.d3rvich.ui.theme.DailyQuizTheme
import ru.d3rvich.ui.theme.FilledStarColor

@Composable
fun QuizResultCard(
    correctAnswers: Int,
    totalQuestions: Int,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable () -> Unit = { DefaultActionButton(onRetryClick) }
) {
    Card(
        shape = RoundedCornerShape(40.dp),
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressStars(correctCount = correctAnswers, totalCount = totalQuestions)
            Text(
                stringResource(R.string.correct_answers, correctAnswers, totalQuestions),
                color = FilledStarColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 20.dp)
            )
            val messageIds = ResultMessage.entries[correctAnswers]
            Text(
                stringResource(messageIds.headlineResId),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                stringResource(messageIds.subtitleResId),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(40.dp))
            actions()
        }
    }
}

@Composable
private fun ProgressStars(correctCount: Int, totalCount: Int, modifier: Modifier = Modifier) {
    require(correctCount >= 0 && totalCount >= 0) { "None of correctCount & totalCount can't be negative" }
    require(correctCount <= totalCount) { "correctCount can't be more than maxCount" }
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        repeat(totalCount) {
            DailyQuizStarIcon((it + 1) <= correctCount, modifier = Modifier.size(48.dp))
        }
    }
}

@Composable
private fun DefaultActionButton(onRetryClick: () -> Unit, modifier: Modifier = Modifier) {
    DailyQuizButton(
        stringResource(id = R.string.restart),
        onClick = onRetryClick,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun QuizResultCardPreview() {
    DailyQuizTheme {
        QuizResultCard(4, 5, {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressStarsPreview() {
    DailyQuizTheme {
        ProgressStars(4, 5)
    }
}