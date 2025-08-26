package ru.d3rvich.quiz.views

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
internal fun QuestionView(
    question: QuestionUiModel,
    progressCount: Int,
    maxQuestions: Int,
    selectedAnswerIndex: Int?,
    showCorrectAnswer: Boolean,
    timerCurrentValue: Long,
    timerMaxValue: Long,
    showTimeoutMessage: Boolean,
    modifier: Modifier = Modifier,
    onAnswerSelect: (index: Int) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
) {
    require(progressCount > 0)
    require(progressCount <= maxQuestions) { "progressCount can't be more than maxQuestion" }
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TopBar(onBackClick = onBackClick)
        Card(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .animateContentSize(),
            shape = RoundedCornerShape(40.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TimerView(
                    timerCurrentValue,
                    timerMaxValue,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                Text(
                    style = MaterialTheme.typography.labelMedium,
                    text = stringResource(R.string.question_count, progressCount, maxQuestions),
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = AnnotatedString.fromHtml(question.text),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                AnswersView(
                    answers = question.answers,
                    selectedAnswerIndex = selectedAnswerIndex,
                    onAnswerSelect = onAnswerSelect,
                    showCorrectAnswer = showCorrectAnswer
                )
                val text = if (progressCount == maxQuestions) {
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
        Text(
            text = stringResource(R.string.warning_massage),
            modifier = Modifier.padding(top = 20.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
    if (showTimeoutMessage) {
        TimeoutMessage(onDismissRequest = onRetryClick)
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
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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
            QuestionView(
                question = entity,
                progressCount = 1,
                maxQuestions = 5,
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