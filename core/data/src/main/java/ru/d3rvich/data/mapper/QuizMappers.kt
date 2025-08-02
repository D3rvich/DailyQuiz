package ru.d3rvich.data.mapper

import ru.d3rvich.database.model.QuizDBO
import ru.d3rvich.domain.entities.QuizResultEntity

internal fun QuizDBO.toQuizResultEntity(): QuizResultEntity = QuizResultEntity(
    id = id,
    generalCategory = category,
    passedTime = passedTime,
    questions = questions.map { it.toQuestionEntity() },
)

internal fun QuizResultEntity.toQuizDBO(): QuizDBO = QuizDBO(
    category = generalCategory,
    passedTime = passedTime,
    questions = questions.map { it.toQuestionDBO() }
)