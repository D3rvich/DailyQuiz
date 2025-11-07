package ru.d3rvich.quiz.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    if (isLandscape) {
        QuestionLandscapeCard(
            question = question,
            currentQuestion = currentQuestion,
            questionsSize = questionsSize,
            selectedAnswerIndex = selectedAnswerIndex,
            showCorrectAnswer = showCorrectAnswer,
            onAnswerSelect = onAnswerSelect,
            onBackClick = onBackClick,
            onNextClick = onNextClick,
            modifier = modifier
        )
    } else {
        QuestionDefaultCard(
            question = question,
            currentQuestion = currentQuestion,
            questionsSize = questionsSize,
            selectedAnswerIndex = selectedAnswerIndex,
            showCorrectAnswer = showCorrectAnswer,
            onAnswerSelect = onAnswerSelect,
            onNextClick = onNextClick,
            modifier = modifier
        )
    }
}

@Composable
private fun QuestionDefaultCard(
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
            QuestionCounter(
                currentQuestionCount = currentQuestion,
                maxQuestions = questionsSize,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            QuestionText(text = question.text, modifier = Modifier.padding(bottom = 16.dp))
            AnswersView(
                answers = question.answers,
                selectedAnswerIndex = selectedAnswerIndex,
                onAnswerSelect = onAnswerSelect,
                showCorrectAnswer = showCorrectAnswer
            )
            CompleteButton(
                currentQuestion = currentQuestion,
                questionsSize = questionsSize,
                selectedAnswerIndex = selectedAnswerIndex,
                onNextClick = onNextClick
            )
        }
    }
}

@Composable
private fun QuestionLandscapeCard(
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
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(
                        painter = painterResource(UiR.drawable.arrow_back_24px),
                        contentDescription = stringResource(R.string.navigate_back)
                    )
                }
                QuestionCounter(
                    currentQuestionCount = currentQuestion,
                    maxQuestions = questionsSize,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            QuestionText(
                text = question.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            AnswersView(
                answers = question.answers,
                selectedAnswerIndex = selectedAnswerIndex,
                showCorrectAnswer = showCorrectAnswer,
                onAnswerSelect = onAnswerSelect
            )
            CompleteButton(
                currentQuestion = currentQuestion,
                questionsSize = questionsSize,
                selectedAnswerIndex = selectedAnswerIndex,
                onNextClick = onNextClick
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
        modifier = modifier.padding(top = 20.dp)
    )
}