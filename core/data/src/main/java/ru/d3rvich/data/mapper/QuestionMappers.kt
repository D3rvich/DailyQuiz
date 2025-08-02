package ru.d3rvich.data.mapper

import ru.d3rvich.domain.entities.AnswerEntity
import ru.d3rvich.domain.entities.QuestionEntity
import ru.d3rvich.network.model.Question
import java.util.UUID

internal inline fun Question.toQuestionEntity(
    answersCombinator: (correctAnswer: String, otherAnswer: List<String>) -> List<AnswerEntity>,
    idGenerator: () -> String = {
        UUID.randomUUID().toString()
    },
): QuestionEntity = QuestionEntity(
    id = idGenerator(),
    category = category,
    text = question,
    answers = answersCombinator(currentAnswer, incorrectAnswers)
)