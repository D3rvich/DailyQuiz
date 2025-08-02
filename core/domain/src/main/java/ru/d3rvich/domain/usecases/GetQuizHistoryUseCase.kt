package ru.d3rvich.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.domain.entities.QuizResultEntity
import ru.d3rvich.domain.repositories.DailyQuizRepository
import javax.inject.Inject

class GetQuizHistoryUseCase @Inject constructor(private val repository: DailyQuizRepository) {
    operator fun invoke(): Flow<List<QuizResultEntity>> = repository.getQuizHistory()
}