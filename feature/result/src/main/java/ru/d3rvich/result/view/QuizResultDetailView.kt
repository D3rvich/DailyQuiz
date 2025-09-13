package ru.d3rvich.result.view

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.result.R
import ru.d3rvich.ui.components.CorrectCheckIcon
import ru.d3rvich.ui.components.DailyQuizButton
import ru.d3rvich.ui.components.QuizResultCard
import ru.d3rvich.ui.model.AnswerUiModel
import ru.d3rvich.ui.model.QuestionUiModel
import ru.d3rvich.ui.model.QuizResultUiModel
import ru.d3rvich.ui.model.correctAnswers
import ru.d3rvich.ui.model.isCorrectAnswer
import ru.d3rvich.ui.theme.DailyQuizTheme
import ru.d3rvich.ui.theme.Grey75
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuizResultDetailView(
    quizResult: QuizResultUiModel,
    onRetryClick: (quizId: Long) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = modifier.fillMaxWidth(),
        contentWindowInsets = WindowInsets(0),
        topBar = {
            QuizResultTopAppBar(
                category = quizResult.generalCategory.text,
                difficult = quizResult.difficult.text,
                scrollBehavior = scrollBehavior,
                onBackClick = onBackClick
            )
        }) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            var buttonPadding by remember { mutableStateOf(0.dp) }
            val state = rememberLazyListState()
            LazyColumn(
                state = state,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                contentPadding = PaddingValues(bottom = buttonPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    QuizResultCard(
                        correctAnswers = quizResult.correctAnswers,
                        totalQuestions = quizResult.questions.size,
                        onRetryClick = { onRetryClick(quizResult.id) },
                    )
                }
                itemsIndexed(
                    quizResult.questions,
                    key = { _, item -> item.hashCode() }) { index, item ->
                    QuestionResultItem(
                        question = item,
                        currentCount = index + 1,
                        totalCount = quizResult.questions.size,
                    )
                }
            }
            val showButton by remember { derivedStateOf { state.firstVisibleItemIndex > 0 } }
            val density = LocalDensity.current
            RetryButton(
                isVisible = showButton,
                onClick = { onRetryClick(quizResult.id) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .onSizeChanged {
                        buttonPadding = density.run { it.height.toDp() }
                    }
            )
        }
    }
}

@Composable
private fun QuestionResultItem(
    question: QuestionUiModel,
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
                    color = Grey75
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
private fun RetryButton(
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bottomWindowInsets = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    var buttonHeight by remember { mutableStateOf(0.dp) }
    val animateDpOffset by animateDpAsState(
        if (isVisible) -buttonHeight - bottomWindowInsets else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )
    val density = LocalDensity.current
    val backgroundColor = MaterialTheme.colorScheme.background
    DailyQuizButton(
        text = stringResource(ru.d3rvich.ui.R.string.retry),
        onClick = onClick,
        modifier = modifier
            .drawWithCache {
                val backgroundBrush = Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        backgroundColor
                    )
                )
                onDrawBehind {
                    drawRect(backgroundBrush)
                }
            }
            .padding(top = 32.dp)
            .padding(horizontal = 40.dp)
            .onSizeChanged {
                buttonHeight = density.run { it.height.toDp() }
            }
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
            .graphicsLayer {
                translationY = density.run { buttonHeight.toPx() + bottomWindowInsets.toPx() }
            }
            .offset(y = animateDpOffset)
    )
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun EmptyQuizResultDetailPreview() {
    DailyQuizTheme {
        val quizResult = QuizResultUiModel(
            generalCategory = Category.AnyCategory,
            difficult = Difficult.AnyDifficulty,
            passedTime = Clock.System.now().toLocalDateTime(
                TimeZone.currentSystemDefault()
            ),
            questions = listOf(),
        )
        QuizResultDetailView(quizResult, {}, {})
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun QuizResultDetailPreview() {
    DailyQuizTheme {
        val answers = List(4) {
            AnswerUiModel("Answer $it", it == 1)
        }
        val question = List(5) {
            QuestionUiModel("Category", "Question $it", answers, (it + 1) % 4)
        }
        val quizResult = QuizResultUiModel(
            generalCategory = Category.AnyCategory,
            difficult = Difficult.AnyDifficulty,
            passedTime = Clock.System.now().toLocalDateTime(
                TimeZone.currentSystemDefault()
            ),
            questions = question,
        )
        QuizResultDetailView(quizResult, {}, {})
    }
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun QuestionCardPreview() {
    DailyQuizTheme {
        val answers = List(4) {
            AnswerUiModel("Answer $it", it == 1)
        }
        val question = QuestionUiModel("Category", "Question", answers, 1)
        Surface(color = MaterialTheme.colorScheme.background) {
            QuestionResultItem(
                question = question,
                currentCount = 1,
                totalCount = 5
            )
        }
    }
}