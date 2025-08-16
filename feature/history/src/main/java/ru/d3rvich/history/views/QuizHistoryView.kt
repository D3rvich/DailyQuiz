package ru.d3rvich.history.views

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.domain.model.SortBy
import ru.d3rvich.history.R
import ru.d3rvich.ui.components.DailyQuizStarIcon
import ru.d3rvich.ui.model.AnswerUiModel
import ru.d3rvich.ui.model.QuestionUiModel
import ru.d3rvich.ui.model.QuizResultUiModel
import ru.d3rvich.ui.model.correctAnswers
import ru.d3rvich.ui.theme.DailyQuizTheme
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuizHistoryView(
    quizList: List<QuizResultUiModel>,
    selectedSort: SortBy,
    onSortChange: (selectedSort: SortBy) -> Unit,
    onQuizCLick: (quizResult: QuizResultUiModel) -> Unit,
    onRemoveQuiz: (quizResult: QuizResultUiModel) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(modifier = modifier, topBar = {
        QuizHistoryTopAppBar(
            selectedSort = selectedSort,
            scrollBehavior = scrollBehavior,
            onSortChange = onSortChange,
            onBackClick = onBackClick
        )
    }) { innerPadding ->
        var showDialog by rememberSaveable { mutableStateOf(false) }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(quizList, key = { it.id }) { item ->
                QuizResultItem(
                    modifier = modifier.animateItem(),
                    quizResult = item,
                    onQuizCLick = { onQuizCLick(item) },
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
    quizResult: QuizResultUiModel,
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
                .pointerInput(quizResult) {
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
                    "Quiz ${quizResult.id}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B0063)
                )
                val correctAnswers = remember { quizResult.correctAnswers }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(quizResult.questions.size) {
                        DailyQuizStarIcon(it + 1 <= correctAnswers, modifier = Modifier.size(18.dp))
                    }
                }
            }
            TimeRow(quizResult.passedTime)
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(
                        R.string.category_placement,
                        quizResult.generalCategory.text
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    stringResource(R.string.difficult_placement, quizResult.difficult.text),
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
            AnswerUiModel("it?", it == 1)
        }
        val questions = List(5) {
            QuestionUiModel("", "it?", answers, it % 4)
        }
        val list = List(8) {
            QuizResultUiModel(
                Category.entries[it],
                Difficult.entries[it % 4],
                Clock.System.now().toLocalDateTime(TimeZone.UTC),
                questions,
                it.toLong()
            )
        }
        QuizHistoryView(list, SortBy.Default(true), { }, {}, {}, {})
    }
}