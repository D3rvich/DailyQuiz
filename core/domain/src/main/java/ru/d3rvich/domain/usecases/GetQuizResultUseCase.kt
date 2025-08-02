package ru.d3rvich.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.model.Result
import ru.d3rvich.domain.repositories.DailyQuizRepository
import javax.inject.Inject

class GetQuizResultUseCase @Inject constructor(private val repository: DailyQuizRepository) {
    operator fun invoke(quizId: Long): Flow<Result<QuizResultEntity>> = repository.getQuizBy(id = quizId)
}