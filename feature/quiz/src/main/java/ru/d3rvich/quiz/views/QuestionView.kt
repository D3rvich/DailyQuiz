package ru.d3rvich.quiz.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.d3rvich.domain.entities.AnswerEntity
import ru.d3rvich.domain.entities.QuestionEntity
import ru.d3rvich.quiz.R
import ru.d3rvich.ui.components.CorrectCheckIcon
import ru.d3rvich.ui.components.DailyQuizButton
import ru.d3rvich.ui.components.DailyQuizLogo
import ru.d3rvich.ui.components.DailyQuizRadioButtonIcon
import ru.d3rvich.ui.theme.DailyQuizTheme

@Composable
internal fun QuestionView(
    question: QuestionEntity,
    progressCount: Int,
    maxQuestions: Int,
    selectedAnswerIndex: Int?,
    showCorrectAnswer: Boolean,
    modifier: Modifier = Modifier,
    onAnswerSelect: (index: Int) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    require(progressCount > 0)
    require(progressCount <= maxQuestions) { "progressCount can't be more than maxQuestion" }
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TopBar(onBackClick = onBackClick)
        Card(
            modifier = Modifier
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
                    text = stringResource(R.string.question_count, progressCount, maxQuestions),
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = Color(0xFFBCB7FF)
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

@Composable
private fun AnswersView(
    answers: List<AnswerEntity>,
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
            val selected = index == selectedAnswerIndex
            AnswerView(
                text = answer.text,
                isSelected = selected,
                isCorrect = if (showCorrectAnswer) answer.isCorrect else null,
                modifier = Modifier.selectable(
                    selected = selected,
                    role = Role.RadioButton,
                    onClick = {
                        onAnswerSelect(index)
                    }
                ))
        }
    }
}

@Composable
private fun AnswerView(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean?,
    modifier: Modifier = Modifier
) {
    val selectedColor = Color(0xFF2B0063)
    val correctColor = Color(0xFF00AE3A)
    val wrongColor = Color(0xFFE70000)
    val borderColor by
    animateColorAsState(
        when {
            isSelected && isCorrect != null && isCorrect -> correctColor
            isSelected && isCorrect != null && !isCorrect -> wrongColor
            isSelected -> selectedColor
            else -> Color.Transparent
        }
    )
    val containerColor by animateColorAsState(
        if (isSelected) CardDefaults.cardColors().containerColor else Color(0xFFF3F3F3)
    )
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(2.dp, color = borderColor),
        colors = CardDefaults.cardColors().copy(containerColor = containerColor)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconToggleButton(isSelected, onCheckedChange = {}, modifier = Modifier.padding(8.dp)) {
                if (!isSelected) {
                    DailyQuizRadioButtonIcon(false)
                } else {
                    isCorrect?.let {
                        CorrectCheckIcon(isCorrect)
                    } ?: DailyQuizRadioButtonIcon(true)
                }
            }
            Text(text = AnnotatedString.fromHtml(text))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AnswerPreview() {
    DailyQuizTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AnswerView(text = "Answer", isSelected = false, isCorrect = null)
            AnswerView(text = "Answer", isSelected = false, isCorrect = true)
            AnswerView(text = "Answer", isSelected = false, isCorrect = false)
            AnswerView(text = "Answer", isSelected = true, isCorrect = true)
            AnswerView(text = "Answer", isSelected = true, isCorrect = false)
        }
    }
}

@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun QuestionPreview() {
    DailyQuizTheme {
        val answers = List(4) { AnswerEntity("Answer #${it + 1}", it == 1) }
        val entity = QuestionEntity(
            "",
            "Lorem ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing elit quisque faucibus.",
            answers,
            0
        )
        QuestionView(
            question = entity,
            progressCount = 1,
            maxQuestions = 5,
            selectedAnswerIndex = null,
            showCorrectAnswer = false,
            onAnswerSelect = {},
            onNextClick = {},
            onBackClick = {})
    }
}