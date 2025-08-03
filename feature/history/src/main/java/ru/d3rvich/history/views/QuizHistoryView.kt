package ru.d3rvich.history.views

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import ru.d3rvich.domain.entities.AnswerEntity
import ru.d3rvich.domain.entities.QuestionEntity
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.entities.correctAnswers
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.history.R
import ru.d3rvich.ui.components.DailyQuizStarIcon
import ru.d3rvich.ui.theme.DailyQuizTheme
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
internal fun QuizHistoryView(
    quizList: List<QuizResultEntity>,
    onQuizCLick: (quizId: Long) -> Unit,
    onRemoveQuiz: (quiz: QuizResultEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) { innerPadding ->
        var showDialog by rememberSaveable { mutableStateOf(false) }
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Box(modifier = Modifier.fillParentMaxWidth(), contentAlignment = Alignment.Center) {
                    ScreenHeadline()
                }
            }
            items(quizList, key = { it.id }) { item ->
                QuizResultItem(
                    modifier = modifier.animateItem(),
                    quizResultEntity = item,
                    onQuizCLick = { onQuizCLick(item.id) },
                    onRemoveQuiz = {
                        showDialog = true
                        onRemoveQuiz(item)
                    },
                )
            }
        }
        if (showDialog) {
            RemoveDialog({ showDialog = false })
        }
    }
}

@Composable
private fun QuizResultItem(
    quizResultEntity: QuizResultEntity,
    onQuizCLick: () -> Unit,
    onRemoveQuiz: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    var cardHeight by remember { mutableStateOf(0.dp) }
    val interactionSource = remember { MutableInteractionSource() }
    val density = LocalDensity.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .onSizeChanged {
                cardHeight = density.run { it.height.toDp() }
            },
        shape = RoundedCornerShape(40.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .indication(interactionSource, LocalIndication.current)
                .pointerInput(quizResultEntity) {
                    detectTapGestures(
                        onLongPress = {
                            showMenu = true
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        },
                        onTap = {
                            onQuizCLick()
                        },
                        onPress = {
                            val press = PressInteraction.Press(it)
                            interactionSource.emit(press)
                            tryAwaitRelease()
                            interactionSource.emit(PressInteraction.Release(press))
                        }
                    )
                }
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
            TimeRow(quizResultEntity.passedTime)
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(
                        R.string.category_placement,
                        quizResultEntity.generalCategory.text
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    stringResource(R.string.difficult_placement, quizResultEntity.difficult.text),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        RemovedItemMenu(
            isVisible = showMenu,
            onVisibilityChange = { showMenu = it },
            onRemove = onRemoveQuiz,
            offset = pressOffset.copy(y = pressOffset.y - cardHeight)
        )
    }
}

@Composable
private fun TimeRow(dataTime: LocalDateTime, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        val dateFormat = remember {
            LocalDateTime.Format {
                day(Padding.NONE)
                char(' ')
                monthName(MonthNames.ENGLISH_FULL)
            }
        }
        val formatDate = dataTime.format(dateFormat)
        Text(formatDate, style = MaterialTheme.typography.bodyMedium)
        val timeFormat = remember {
            LocalDateTime.Format {
                hour()
                char(':')
                minute()
            }
        }
        val formatTime = dataTime.format(timeFormat)
        Text(formatTime, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun RemovedItemMenu(
    isVisible: Boolean,
    onVisibilityChange: (Boolean) -> Unit,
    onRemove: () -> Unit,
    offset: DpOffset,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        isVisible,
        onDismissRequest = { onVisibilityChange(false) },
        modifier = modifier,
        offset = offset,
    ) {
        Row(
            modifier = Modifier
                .width(200.dp)
                .clickable(onClick = {
                    onRemove()
                    onVisibilityChange(false)
                }),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.ic_delete),
                contentDescription = stringResource(R.string.remove_quiz),
                modifier = Modifier
                    .padding(8.dp)
                    .padding(horizontal = 12.dp)
            )
            Text(stringResource(R.string.remove))
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
                Category.entries[it],
                Difficult.entries[it % 4],
                Clock.System.now().toLocalDateTime(TimeZone.UTC),
                questions,
                it.toLong()
            )
        }
        QuizHistoryView(list, {}, {})
    }
}