package ru.d3rvich.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.domain.model.Result
import ru.d3rvich.domain.repositories.DailyQuizRepository
import javax.inject.Inject

class GetQuizUseCase @Inject constructor(private val repository: DailyQuizRepository) {
    suspend operator fun invoke(): Flow<Result<QuizEntity>> = repository.getQuiz()
}