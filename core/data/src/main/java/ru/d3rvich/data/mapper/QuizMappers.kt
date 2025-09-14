package ru.d3rvich.data.mapper

import ru.d3rvich.database.model.QuestionDBO
import ru.d3rvich.database.model.QuizDBO
import ru.d3rvich.domain.entities.QuestionEntity
import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.domain.entities.QuizResultEntity

internal fun QuizDBO.toQuizResultEntity(): QuizResultEntity = QuizResultEntity(
    id = id,
    generalCategory = category,
    difficulty = difficulty,
    passedTime = passedTime,
    questions = questions.map(QuestionDBO::toQuestionEntity),
)

internal fun QuizDBO.toQuizEntity(disableAnswers: Boolean): QuizEntity =
    QuizEntity(
        generalCategory = category,
        difficulty = difficulty,
        questions = questions.map { quiz ->
            quiz.toQuestionEntity()
                .let { question -> if (disableAnswers) question.copy(selectedAnswerIndex = null) else question }
        })

internal fun QuizResultEntity.toQuizDBO(): QuizDBO = QuizDBO(
    id = id,
    category = generalCategory,
    difficulty = difficulty,
    passedTime = passedTime,
    questions = questions.map(QuestionEntity::toQuestionDBO)
)