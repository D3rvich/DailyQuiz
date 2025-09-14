package ru.d3rvich.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty
import ru.d3rvich.domain.model.Result
import ru.d3rvich.domain.model.SortBy

interface DailyQuizRepository {
    fun getExistedOrNewQuiz(
        quizId: Long?,
        category: Category,
        difficulty: Difficulty
    ): Flow<Result<QuizEntity>>

    suspend fun saveQuiz(quizResult: QuizResultEntity)

    fun getQuizHistory(sortBy: SortBy): Flow<List<QuizResultEntity>>

    fun getQuizBy(id: Long): Flow<Result<QuizResultEntity>>

    suspend fun removeQuiz(quizResult: QuizResultEntity)
}