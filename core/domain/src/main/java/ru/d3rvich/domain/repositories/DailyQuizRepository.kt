package ru.d3rvich.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.model.Result

interface DailyQuizRepository {
    suspend fun getQuiz(): Flow<Result<QuizEntity>>

    suspend fun saveQuizResult(quizResult: QuizResultEntity)

    suspend fun getSavedQuiz(): QuizResultEntity
}