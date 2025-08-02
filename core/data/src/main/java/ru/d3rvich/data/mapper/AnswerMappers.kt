package ru.d3rvich.data.mapper

import ru.d3rvich.database.model.AnswerDBO
import ru.d3rvich.domain.entities.AnswerEntity

internal fun AnswerDBO.toAnswerEntity(): AnswerEntity =
    AnswerEntity(text = text, isCorrect = isCorrect)

internal fun AnswerEntity.toAnswerDBO(): AnswerDBO = AnswerDBO(text = text, isCorrect = isCorrect)