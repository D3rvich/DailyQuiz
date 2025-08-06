package ru.d3rvich.ui.mappers

import ru.d3rvich.domain.entities.AnswerEntity
import ru.d3rvich.domain.entities.QuestionEntity
import ru.d3rvich.ui.model.AnswerUiModel
import ru.d3rvich.ui.model.QuestionUiModel

fun QuestionEntity.toQuestionUiModel(): QuestionUiModel =
    QuestionUiModel(
        category = category,
        text = text,
        answers = answers.map(AnswerEntity::toAnswerUiModel),
        selectedAnswerIndex = selectedAnswerIndex
    )

fun QuestionUiModel.toQuestionEntity(): QuestionEntity =
    QuestionEntity(
        category = category,
        text = text,
        answers = answers.map(AnswerUiModel::toAnswerEntity),
        selectedAnswerIndex = selectedAnswerIndex
    )