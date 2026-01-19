package ru.d3rvich.quiz.impl.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import ru.d3rvich.quiz.impl.R
import ru.d3rvich.quiz.impl.screens.quiz.TimerMaxValue
import ru.d3rvich.ui.model.AnswerUiModel
import ru.d3rvich.ui.model.QuestionUiModel
import ru.d3rvich.ui.theme.DailyQuizTheme
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
internal fun QuizView(
    questions: ImmutableList<QuestionUiModel>,
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
    val isLandscape = !(currentWindowAdaptiveInfo().windowSizeClass
        .isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND))
    Scaffold(
        modifier = modifier,
        topBar = {
            if (!isLandscape) {
                TopBar(onBackClick = onBackClick)
            }
        },
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
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(modifier = Modifier.widthIn(max = if (isLandscape) Dp.Unspecified else 640.dp)) {
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
                HorizontalPager(state, userScrollEnabled = false) { currentIndex ->
                    val question = questions[currentIndex]
                    QuestionCard(
                        isLandscape = isLandscape,
                        question = question,
                        currentQuestion = currentQuestionIndex + 1,
                        questionsSize = questions.size,
                        selectedAnswerIndex = selectedAnswerIndex,
                        showCorrectAnswer = showCorrectAnswer,
                        onAnswerSelect = onAnswerSelect,
                        onNextClick = onNextClick,
                        onBackClick = onBackClick,
                    )
                }
            }
        }
    }
    if (showTimeoutMessage) {
        TimeoutMessage(onDismissRequest = onRetryClick)
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

@PreviewLightDark
@PreviewDynamicColors
@PreviewScreenSizes
@Composable
private fun QuestionPreview() {
    DailyQuizTheme {
        val answers = List(4) { AnswerUiModel("Answer #${it + 1}", it == 1) }.toPersistentList()
        val entity = QuestionUiModel(
            "",
            "Lorem ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing elit quisque faucibus.",
            answers,
            0
        )
        Surface(color = MaterialTheme.colorScheme.background) {
            QuizView(
                questions = persistentListOf(entity),
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