package ru.d3rvich.quiz.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import ru.d3rvich.quiz.R
import ru.d3rvich.ui.components.QuizResultCard
import ru.d3rvich.ui.theme.DailyQuizTheme

@Composable
internal fun ResultsView(
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
        QuizResultCard(
            correctAnswers = correctAnswers,
            totalQuestions = totalQuestions,
            onRetryClick = onRetryClick
        )
    }
}

@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun ResultsPreview() {
    DailyQuizTheme {
        ResultsView(correctAnswers = 4, totalQuestions = 5) {}
    }
}