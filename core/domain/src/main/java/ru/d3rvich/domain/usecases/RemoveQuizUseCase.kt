package ru.d3rvich.domain.usecases

import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.repositories.DailyQuizRepository
import javax.inject.Inject

class RemoveQuizUseCase @Inject constructor(private val repository: DailyQuizRepository) {
    suspend operator fun invoke(quizResultEntity: QuizResultEntity) =
        repository.saveQuiz(quizResultEntity)
}