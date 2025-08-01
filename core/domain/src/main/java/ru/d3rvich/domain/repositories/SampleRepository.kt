package ru.d3rvich.domain.repositories

import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.domain.entities.QuizResultEntity

interface SampleRepository {
    suspend fun getQuiz(): QuizEntity

    suspend fun saveQuizResult(quizResult: QuizResultEntity)

    suspend fun getSavedQuiz(): QuizResultEntity
}