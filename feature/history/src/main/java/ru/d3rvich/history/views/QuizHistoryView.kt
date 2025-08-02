package ru.d3rvich.history.views

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import ru.d3rvich.domain.entities.AnswerEntity
import ru.d3rvich.domain.entities.QuestionEntity
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.entities.correctAnswers
import ru.d3rvich.ui.components.DailyQuizStarIcon
import ru.d3rvich.ui.theme.DailyQuizTheme
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
internal fun QuizHistoryView(
    quizList: List<QuizResultEntity>,
    onQuizCLick: (quizId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            item(key = "Headline") {
                Box(modifier = Modifier.fillParentMaxWidth(), contentAlignment = Alignment.Center) {
                    ScreenHeadline()
                }
            }
            items(quizList) { item ->
                QuizResultItem(
                    item,
                    modifier = Modifier.combinedClickable(onClick = { onQuizCLick(item.id) })
                )
            }
        }
    }
}

@Composable
private fun QuizResultItem(
    quizResultEntity: QuizResultEntity,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 20.dp), shape = RoundedCornerShape(40.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Quiz ${quizResultEntity.id}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B0063)
                )
                val correctAnswers = remember { quizResultEntity.correctAnswers }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(quizResultEntity.questions.size) {
                        DailyQuizStarIcon(it + 1 <= correctAnswers, modifier = Modifier.size(18.dp))
                    }
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                val dateFormat = remember {
                    LocalDateTime.Format {
                        day()
                        char(' ')
                        monthName(MonthNames.ENGLISH_FULL)
                    }
                }
                val formatDate = quizResultEntity.passedTime.format(dateFormat)
                Text(formatDate, style = MaterialTheme.typography.bodyMedium)
                val timeFormat = remember {
                    LocalDateTime.Format {
                        hour()
                        char(':')
                        minute()
                    }
                }
                val formatTime = quizResultEntity.passedTime.format(timeFormat)
                Text(formatTime, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun QuizHistoryViewPreview() {
    DailyQuizTheme {
        val answers = List(4) {
            AnswerEntity("it?", it == 1)
        }
        val questions = List(5) {
            QuestionEntity("", "it?", answers, it % 4)
        }
        val list = List(8) {
            QuizResultEntity(
                it.toLong(),
                "",
                Clock.System.now().toLocalDateTime(TimeZone.UTC),
                questions
            )
        }
        QuizHistoryView(list, {})
    }
}