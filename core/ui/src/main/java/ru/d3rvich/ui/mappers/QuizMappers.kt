package ru.d3rvich.ui.mappers

import kotlinx.collections.immutable.toPersistentList
import ru.d3rvich.domain.entities.QuestionEntity
import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.ui.model.QuizUiModel

fun QuizEntity.toQuizUiModel(): QuizUiModel =
    QuizUiModel(
        category = generalCategory,
        difficulty = difficulty,
        questions = questions.map(QuestionEntity::toQuestionUiModel).toPersistentList(),
    )