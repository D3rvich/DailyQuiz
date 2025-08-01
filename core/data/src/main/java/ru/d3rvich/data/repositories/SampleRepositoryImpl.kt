package ru.d3rvich.data.repositories

import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.repositories.SampleRepository

internal class SampleRepositoryImpl : SampleRepository {
    override suspend fun getQuiz(): QuizEntity {
        TODO("Not yet implemented")
    }

    override suspend fun saveQuizResult(quizResult: QuizResultEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedQuiz(): QuizResultEntity {
        TODO("Not yet implemented")
    }
}