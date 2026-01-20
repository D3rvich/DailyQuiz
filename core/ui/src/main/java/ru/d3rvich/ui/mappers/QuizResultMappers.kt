package ru.d3rvich.ui.mappers

import kotlinx.collections.immutable.toPersistentList
import ru.d3rvich.domain.entities.QuestionEntity
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.ui.model.QuestionUiModel
import ru.d3rvich.ui.model.QuizResultUiModel

fun QuizResultEntity.toQuizResultUiModel(): QuizResultUiModel =
    QuizResultUiModel(
        generalCategory = generalCategory,
        difficulty = difficulty,
        passedTime = passedTime,
        questions = questions.map(QuestionEntity::toQuestionUiModel).toPersistentList(),
        correctAnswers = correctAnswers,
        id = id
    )

fun QuizResultUiModel.toQuizResultEntity(): QuizResultEntity =
    QuizResultEntity(
        generalCategory = generalCategory,
        difficulty = difficulty,
        passedTime = passedTime,
        questions = questions.map(QuestionUiModel::toQuestionEntity),
        correctAnswers = correctAnswers,
        id = id
    )