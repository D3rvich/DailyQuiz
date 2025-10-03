package ru.d3rvich.quiz.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import ru.d3rvich.quiz.R
import ru.d3rvich.quiz.TimerMaxValue
import ru.d3rvich.ui.components.AnswerType
import ru.d3rvich.ui.components.AnswerUiCard
import ru.d3rvich.ui.components.DailyQuizButton
import ru.d3rvich.ui.components.DailyQuizLogo
import ru.d3rvich.ui.model.AnswerUiModel
import ru.d3rvich.ui.model.QuestionUiModel
import ru.d3rvich.ui.theme.DailyQuizTheme
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
internal fun QuizView(
    questions: List<QuestionUiModel>,
    currentQuestionIndex: Int,
    selectedAnswerIndex: Int?,
    showCorrectAnswer: Boolean,
    timerCurrentValue: Long,
    timerMaxValue: Long,
    showTimeoutMessage: Boolean,
    onAnswerSelect: (index: Int) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopBar(onBackClick = onBackClick) },
        bottomBar = {
            Text(
                text = stringResource(R.string.warning_massage),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
                    .padding(bottom = 4.dp, top = 12.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TimerView(
                currentValue = timerCurrentValue,
                maxValue = timerMaxValue,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 24.dp)
            )
            val state = rememberPagerState { questions.size }
            LaunchedEffect(currentQuestionIndex) {
                state.animateScrollToPage(currentQuestionIndex)
            }
            HorizontalPager(state, userScrollEnabled = false) { page ->
                val question = questions[currentQuestionIndex]
                QuestionCard(
                    question = question,
                    currentQuestion = currentQuestionIndex + 1,
                    questionsSize = questions.size,
                    selectedAnswerIndex = selectedAnswerIndex,
                    showCorrectAnswer = showCorrectAnswer,
                    onAnswerSelect = onAnswerSelect,
                    onNextClick = onNextClick
                )
            }
        }
    }
    if (showTimeoutMessage) {
        TimeoutMessage(onDismissRequest = onRetryClick)
    }
}

@Composable
private fun QuestionCard(
    question: QuestionUiModel,
    currentQuestion: Int,
    questionsSize: Int,
    selectedAnswerIndex: Int?,
    showCorrectAnswer: Boolean,
    onAnswerSelect: (Int) -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(40.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                style = MaterialTheme.typography.labelMedium,
                text = stringResource(
                    R.string.question_count,
                    currentQuestion,
                    questionsSize
                ),
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = AnnotatedString.fromHtml(question.text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 5,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            AnswersView(
                answers = question.answers,
                selectedAnswerIndex = selectedAnswerIndex,
                onAnswerSelect = onAnswerSelect,
                showCorrectAnswer = showCorrectAnswer
            )
            val text = if (currentQuestion == questionsSize) {
                stringResource(R.string.complete)
            } else {
                stringResource(R.string.next)
            }
            DailyQuizButton(
                text = text,
                onClick = onNextClick,
                enabled = selectedAnswerIndex != null,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}

@Composable
private fun TopBar(modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    Box(
        modifier
            .padding(vertical = 32.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 12.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = stringResource(R.string.navigate_back)
            )
        }
        DailyQuizLogo(
            modifier = Modifier
                .align(Alignment.Center)
                .height(40.dp)
        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun TimerView(currentValue: Long, maxValue: Long, modifier: Modifier = Modifier) {
    require(maxValue > 0) { "Timer max value had to be greater than zero." }
    require(currentValue <= maxValue) { "Timer current value can't me greater it's max value." }
    Column(modifier.fillMaxWidth()) {
        val currentDateTime = Instant.fromEpochMilliseconds(currentValue)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val maxDataTime = Instant.fromEpochMilliseconds(maxValue)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val formater = remember {
            LocalDateTime.Format {
                minute(Padding.ZERO)
                char(':')
                second(Padding.ZERO)
            }
        }
        val progress = currentValue.toFloat() / maxValue
        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                currentDateTime.format(formater),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                maxDataTime.format(formater),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun AnswersView(
    answers: List<AnswerUiModel>,
    selectedAnswerIndex: Int?,
    showCorrectAnswer: Boolean,
    modifier: Modifier = Modifier,
    onAnswerSelect: (Int) -> Unit
) {
    Column(
        modifier = modifier.padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        answers.forEachIndexed { index, answer ->
            val isSelected = index == selectedAnswerIndex
            val type = when {
                isSelected && !showCorrectAnswer -> AnswerType.Selected
                isSelected && answer.isCorrect -> AnswerType.Correct
                isSelected && !answer.isCorrect -> AnswerType.Wrong
                else -> AnswerType.NotSelected
            }
            AnswerUiCard(
                text = answer.text,
                answerType = type,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .selectable(
                        selected = isSelected,
                        role = Role.RadioButton,
                        onClick = {
                            onAnswerSelect(index)
                        }
                    ))
        }
    }
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun QuestionPreview() {
    DailyQuizTheme {
        val answers = List(4) { AnswerUiModel("Answer #${it + 1}", it == 1) }
        val entity = QuestionUiModel(
            "",
            "Lorem ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing elit quisque faucibus.",
            answers,
            0
        )
        Surface(color = MaterialTheme.colorScheme.background) {
            QuizView(
                questions = listOf(entity),
                currentQuestionIndex = 0,
                timerCurrentValue = 20000L,
                timerMaxValue = TimerMaxValue,
                selectedAnswerIndex = null,
                showCorrectAnswer = false,
                showTimeoutMessage = false,
                onAnswerSelect = {},
                onNextClick = {},
                onBackClick = {},
                onRetryClick = {})
        }
    }
}