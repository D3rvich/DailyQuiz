package ru.d3rvich.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.d3rvich.data.mapper.toQuestionEntity
import ru.d3rvich.data.mapper.toQuizDBO
import ru.d3rvich.data.mapper.toQuizResultEntity
import ru.d3rvich.database.DailyQuizDatabase
import ru.d3rvich.domain.entities.AnswerEntity
import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.domain.model.Result
import ru.d3rvich.domain.model.asResult
import ru.d3rvich.domain.repositories.DailyQuizRepository
import ru.d3rvich.network.DailyQuizNetworkDataSource
import ru.d3rvich.network.result.NetworkResult

internal class DailyQuizRepositoryImpl(
    private val networkDataSource: DailyQuizNetworkDataSource,
    private val database: DailyQuizDatabase
) : DailyQuizRepository {
    override suspend fun getNewQuiz(
        category: Category,
        difficult: Difficult
    ): Flow<Result<QuizEntity>> = flow {
        when (val result = networkDataSource.getQuiz(DefaultQuestionsCount, category, difficult)) {
            is NetworkResult.Failure -> throw result.exception
            is NetworkResult.Success -> {
                val questions = result.value.map { it.toQuestionEntity(::combineAnswers) }
                emit(
                    QuizEntity(
                        generalCategory = category,
                        difficult = difficult,
                        questions = questions
                    )
                )
            }
        }
    }.asResult()

    override suspend fun saveQuiz(quizResult: QuizResultEntity) =
        database.quizDao.saveQuiz(quizResult.toQuizDBO())

    override fun getQuizHistory(): Flow<List<QuizResultEntity>> =
        database.quizDao.getQuizHistory().map { list -> list.map { it.toQuizResultEntity() } }

    override fun getQuizBy(id: Long): Flow<Result<QuizResultEntity>> = flow {
        emit((database.quizDao.getQuizBy(id = id).toQuizResultEntity()))
    }.asResult()

    override suspend fun removeQuiz(quizResult: QuizResultEntity) =
        database.quizDao.removeQuiz(quizResult.toQuizDBO())
}

private fun combineAnswers(currentAnswer: String, otherAnswers: List<String>): List<AnswerEntity> =
    (otherAnswers.map { AnswerEntity(it, false) } + AnswerEntity(currentAnswer, true)).shuffled()

private const val DefaultQuestionsCount = 5