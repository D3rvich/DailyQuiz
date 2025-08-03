package ru.d3rvich.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.domain.model.Result

interface DailyQuizRepository {
    suspend fun getNewQuiz(category: Category, difficult: Difficult): Flow<Result<QuizEntity>>

    suspend fun saveQuiz(quizResult: QuizResultEntity)

    fun getQuizHistory(): Flow<List<QuizResultEntity>>

    fun getQuizBy(id: Long): Flow<Result<QuizResultEntity>>

    suspend fun removeQuiz(quizResult: QuizResultEntity)
}