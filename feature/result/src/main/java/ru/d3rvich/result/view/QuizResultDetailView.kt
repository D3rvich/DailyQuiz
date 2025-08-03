package ru.d3rvich.result.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.d3rvich.domain.entities.AnswerEntity
import ru.d3rvich.domain.entities.QuestionEntity
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.entities.correctAnswers
import ru.d3rvich.domain.entities.isCorrectAnswer
import ru.d3rvich.result.R
import ru.d3rvich.ui.components.CorrectCheckIcon
import ru.d3rvich.ui.components.QuizResultCard
import ru.d3rvich.ui.theme.DailyQuizTheme
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
internal fun QuizResultDetailView(
    quizResult: QuizResultEntity,
    onRetryClick: (quizId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxWidth()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical)
                .asPaddingValues(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.results),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = TextUnit(0f, TextUnitType.Sp),
                        modifier = modifier.padding(bottom = 40.dp, top = 52.dp)
                    )
                    QuizResultCard(
                        correctAnswers = quizResult.correctAnswers,
                        totalQuestions = quizResult.questions.size,
                        onRetryClick = { onRetryClick(quizResult.id) }
                    )
                }
            }
            itemsIndexed(
                quizResult.questions,
                key = { index, item -> item.hashCode() }) { index, item ->
                QuestionResultItem(
                    question = item,
                    currentCount = index + 1,
                    totalCount = quizResult.questions.size,
                )
            }
            item {
                RetryButton({ onRetryClick(quizResult.id) })
            }
        }
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                .windowInsetsTopHeight(WindowInsets.safeDrawing)
                .fillMaxWidth()
                .align(Alignment.TopStart)
        )
    }
}

@Composable
private fun QuestionResultItem(
    question: QuestionEntity,
    currentCount: Int,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    require(currentCount > 0 && totalCount > 0)
    require(currentCount <= totalCount)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp), shape = RoundedCornerShape(40.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.question_progress, currentCount, totalCount),
                    color = Color(0xFFBABABA)
                )
                CorrectCheckIcon(question.isCorrectAnswer)
            }
            Text(
                text = AnnotatedString.fromHtml(question.text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            AnswersView(question.answers, question.selectedAnswerIndex ?: -1)
        }
    }
}

@Composable
private fun RetryButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 40.dp)
            .fillMaxWidth()
            .padding(bottom = 40.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors()
            .copy(containerColor = Color.White, contentColor = Color(0xFF2B0063))
    ) {
        Text(
            text = stringResource(ru.d3rvich.ui.R.string.retry).uppercase(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun EmptyQuizResultDetailPreview() {
    DailyQuizTheme {
        val quizResult = QuizResultEntity(
            generalCategory = "",
            passedTime = Clock.System.now().toLocalDateTime(
                TimeZone.currentSystemDefault()
            ),
            questions = emptyList(),
        )
        QuizResultDetailView(quizResult, {})
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun QuizResultDetailPreview() {
    DailyQuizTheme {
        val answers = List(4) {
            AnswerEntity("Answer $it", it == 1)
        }
        val question = List(5) {
            QuestionEntity("Category", "Question $it", answers, (it + 1) % 4)
        }
        val quizResult = QuizResultEntity(
            generalCategory = "",
            passedTime = Clock.System.now().toLocalDateTime(
                TimeZone.currentSystemDefault()
            ),
            questions = question,
        )
        QuizResultDetailView(quizResult, {})
    }
}