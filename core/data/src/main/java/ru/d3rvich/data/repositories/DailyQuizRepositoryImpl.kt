package ru.d3rvich.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.d3rvich.data.mapper.toQuestionEntity
import ru.d3rvich.data.mapper.toQuizDBO
import ru.d3rvich.data.mapper.toQuizEntity
import ru.d3rvich.data.mapper.toQuizResultEntity
import ru.d3rvich.database.DailyQuizDatabase
import ru.d3rvich.database.dao.SortByRaw
import ru.d3rvich.database.model.QuizDBO
import ru.d3rvich.domain.entities.AnswerEntity
import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.domain.model.Result
import ru.d3rvich.domain.model.SortBy
import ru.d3rvich.domain.model.asResult
import ru.d3rvich.domain.repositories.DailyQuizRepository
import ru.d3rvich.network.DailyQuizNetworkDataSource
import ru.d3rvich.network.result.NetworkResult

internal class DailyQuizRepositoryImpl(
    private val networkDataSource: DailyQuizNetworkDataSource,
    private val database: DailyQuizDatabase
) : DailyQuizRepository {
    override fun getExistedOrNewQuiz(
        quizId: Long?,
        category: Category,
        difficult: Difficult
    ): Flow<Result<QuizEntity>> = flow {
        quizId?.also {
            emit(database.quizDao.getQuizBy(quizId).toQuizEntity(true))
        } ?: when (val result =
            networkDataSource.getQuiz(DefaultQuestionsAmount, category, difficult)) {
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

    override fun getQuizHistory(sortBy: SortBy): Flow<List<QuizResultEntity>> =
        database.quizDao.getQuizHistory(sortBy = sortBy.rawValue, isAsc = sortBy.byAscending)
            .map { list -> list.map(QuizDBO::toQuizResultEntity) }

    override fun getQuizBy(id: Long): Flow<Result<QuizResultEntity>> = flow {
        emit((database.quizDao.getQuizBy(id = id).toQuizResultEntity()))
    }.asResult()

    override suspend fun removeQuiz(quizResult: QuizResultEntity) =
        database.quizDao.removeQuiz(quizResult.toQuizDBO())

    private fun combineAnswers(
        currentAnswer: String,
        otherAnswers: List<String>
    ): List<AnswerEntity> =
        (otherAnswers.map { AnswerEntity(it, false) }
                + AnswerEntity(currentAnswer, true)).shuffled()
}

private val SortBy.rawValue: String
    get() = when (this) {
        is SortBy.Default -> SortByRaw.DEFAULT
        is SortBy.PassedTime -> SortByRaw.PASSED_TIME
    }

private const val DefaultQuestionsAmount = 5