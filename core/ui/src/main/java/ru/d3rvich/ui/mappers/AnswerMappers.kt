package ru.d3rvich.ui.mappers

import ru.d3rvich.domain.entities.AnswerEntity
import ru.d3rvich.ui.model.AnswerUiModel

fun AnswerEntity.toAnswerUiModel(): AnswerUiModel = AnswerUiModel(text, isCorrect)

fun AnswerUiModel.toAnswerEntity(): AnswerEntity = AnswerEntity(text, isCorrect)