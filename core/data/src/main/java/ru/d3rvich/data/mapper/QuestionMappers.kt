package ru.d3rvich.data.mapper

import ru.d3rvich.database.model.AnswerDBO
import ru.d3rvich.database.model.QuestionDBO
import ru.d3rvich.domain.entities.AnswerEntity
import ru.d3rvich.domain.entities.QuestionEntity
import ru.d3rvich.network.model.Question
import kotlin.random.Random

internal inline fun Question.toQuestionEntity(
    answersCombinator: (correctAnswer: String, otherAnswer: List<String>) -> List<AnswerEntity>,
): QuestionEntity = QuestionEntity(
    category = category,
    text = question,
    answers = answersCombinator(currentAnswer, incorrectAnswers)
)

internal fun QuestionDBO.toQuestionEntity(): QuestionEntity = QuestionEntity(
    category = category,
    text = text,
    answers = answers.map(AnswerDBO::toAnswerEntity),
    selectedAnswerIndex = selectedAnswerIndex
)

internal fun QuestionEntity.toQuestionDBO(): QuestionDBO = QuestionDBO(
    category = category,
    text = text,
    answers = answers.map(AnswerEntity::toAnswerDBO),
    selectedAnswerIndex = selectedAnswerIndex ?: Random.nextInt(answers.lastIndex)
)