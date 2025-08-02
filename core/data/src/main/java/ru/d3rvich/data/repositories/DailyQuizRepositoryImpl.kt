package ru.d3rvich.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.d3rvich.data.mapper.toQuestionEntity
import ru.d3rvich.data.mapper.toQuizDBO
import ru.d3rvich.data.mapper.toQuizResultEntity
import ru.d3rvich.database.DailyQuizDatabase
import ru.d3rvich.domain.entities.AnswerEntity
import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.model.Result
import ru.d3rvich.domain.model.asResult
import ru.d3rvich.domain.repositories.DailyQuizRepository
import ru.d3rvich.network.DailyQuizNetworkDataSource
import ru.d3rvich.network.result.NetworkResult
import java.util.UUID

internal class DailyQuizRepositoryImpl(
    private val networkDataSource: DailyQuizNetworkDataSource,
    private val database: DailyQuizDatabase
) : DailyQuizRepository {
    override suspend fun getQuiz(): Flow<Result<QuizEntity>> = flow {
        when (val result = networkDataSource.getQuiz(DefaultQuestionsCount)) {
            is NetworkResult.Failure -> Result.Error(result.exception)
            is NetworkResult.Success -> {
                val questions = result.value.map { it.toQuestionEntity(::combineAnswers) }
                emit(QuizEntity(UUID.randomUUID().toString(), "", questions))
            }
        }
    }.asResult()

    override suspend fun saveQuiz(quizResult: QuizResultEntity) =
        database.quizDao.saveQuiz(quizResult.toQuizDBO())

    override suspend fun getQuizList(): List<QuizResultEntity> =
        database.quizDao.getQuizList().map { it.toQuizResultEntity() }

    override suspend fun getQuizBy(id: Long): QuizResultEntity =
        database.quizDao.getQuizBy(id = id).toQuizResultEntity()
}

private fun combineAnswers(currentAnswer: String, otherAnswers: List<String>): List<AnswerEntity> =
    (otherAnswers.map { AnswerEntity(it, false) } + AnswerEntity(currentAnswer, true)).shuffled()

private const val DefaultQuestionsCount = 5