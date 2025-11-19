package ru.d3rvich.quiz.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import ru.d3rvich.quiz.R
import ru.d3rvich.ui.components.AnswerType
import ru.d3rvich.ui.components.AnswerUiCard
import ru.d3rvich.ui.components.DailyQuizButton
import ru.d3rvich.ui.model.AnswerUiModel
import ru.d3rvich.ui.model.QuestionUiModel
import ru.d3rvich.ui.R as UiR

@Composable
internal fun QuestionCard(
    isLandscape: Boolean,
    question: QuestionUiModel,
    currentQuestion: Int,
    questionsSize: Int,
    selectedAnswerIndex: Int?,
    showCorrectAnswer: Boolean,
    onAnswerSelect: (Int) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(40.dp)
    ) {
        val isCompatWidth = !(currentWindowAdaptiveInfo().windowSizeClass
            .isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND))
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (isCompatWidth) 1 else 2),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 24.dp,
                vertical = if (isLandscape) 12.dp else 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (isLandscape) {
                            IconButton(
                                onBackClick,
                                modifier = Modifier.align(Alignment.CenterStart)
                            ) {
                                Icon(
                                    painter = painterResource(UiR.drawable.arrow_back_24px),
                                    contentDescription = stringResource(R.string.navigate_back)
                                )
                            }
                        }
                        QuestionCounter(
                            currentQuestionCount = currentQuestion,
                            maxQuestions = questionsSize,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(bottom = if (isLandscape) 0.dp else 12.dp)
                        )
                    }
                    QuestionText(text = question.text)
                }
            }
            answersView(
                answers = question.answers,
                selectedAnswerIndex = selectedAnswerIndex,
                onAnswerSelect = onAnswerSelect,
                showCorrectAnswer = showCorrectAnswer
            )
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                CompleteButton(
                    currentQuestion = currentQuestion,
                    questionsSize = questionsSize,
                    selectedAnswerIndex = selectedAnswerIndex,
                    onNextClick = onNextClick,
                    modifier = Modifier.padding(top = if (isLandscape) 0.dp else 16.dp)
                )
            }
        }
    }
}

private fun LazyGridScope.answersView(
    answers: List<AnswerUiModel>,
    selectedAnswerIndex: Int?,
    showCorrectAnswer: Boolean,
    onAnswerSelect: (Int) -> Unit
) {
    itemsIndexed(answers) { index, answer ->
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

@Composable
private fun QuestionCounter(
    currentQuestionCount: Int,
    maxQuestions: Int,
    modifier: Modifier = Modifier
) {
    Text(
        style = MaterialTheme.typography.labelMedium,
        text = stringResource(
            R.string.question_count,
            currentQuestionCount,
            maxQuestions
        ),
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
private fun QuestionText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = AnnotatedString.fromHtml(text),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelLarge,
        maxLines = 5,
        modifier = modifier
    )
}

@Composable
private fun CompleteButton(
    currentQuestion: Int,
    questionsSize: Int,
    selectedAnswerIndex: Int?,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    require(currentQuestion <= questionsSize)
    val text = if (currentQuestion == questionsSize) {
        stringResource(R.string.complete)
    } else {
        stringResource(R.string.next)
    }
    DailyQuizButton(
        text = text,
        onClick = onNextClick,
        enabled = selectedAnswerIndex != null,
        modifier = modifier
    )
}