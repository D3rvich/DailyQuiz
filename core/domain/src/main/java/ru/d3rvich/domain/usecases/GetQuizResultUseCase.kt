package ru.d3rvich.domain.usecases

import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.repositories.DailyQuizRepository
import javax.inject.Inject

class GetQuizResultUseCase @Inject constructor(private val repository: DailyQuizRepository) {
    suspend operator fun invoke(quizId: Long): QuizResultEntity = repository.getQuizBy(id = quizId)
}