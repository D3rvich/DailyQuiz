package ru.d3rvich.detail.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import ru.d3rvich.quiz.R
import ru.d3rvich.ui.components.DailyQuizButton
import ru.d3rvich.ui.components.DailyQuizStarIcon
import ru.d3rvich.ui.model.ResultMessage
import ru.d3rvich.ui.theme.DailyQuizTheme

@Composable
fun ResultsView(
    correctAnswers: Int,
    totalQuestions: Int,
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.results),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = TextUnit(0f, TextUnitType.Sp),
            modifier = Modifier.padding(40.dp)
        )
        Card(
            shape = RoundedCornerShape(40.dp), modifier = Modifier
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
                    color = Color(0xFFFFB800),
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
                DailyQuizButton(
                    stringResource(id = R.string.retry),
                    onClick = onRetryClick,
                    modifier = Modifier.padding(top = 40.dp)
                )
            }
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

@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun ResultsPreview() {
    DailyQuizTheme {
        ResultsView(correctAnswers = 4, totalQuestions = 5) {}
    }
}

@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun ProgressStarsPreview() {
    DailyQuizTheme {
        ProgressStars(4, 5)
    }
}