package ru.d3rvich.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.domain.entities.QuizEntity
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.domain.model.Result
import ru.d3rvich.domain.repositories.DailyQuizRepository
import javax.inject.Inject

class GetExistedOrNewQuizUseCase @Inject constructor(private val repository: DailyQuizRepository) {
    operator fun invoke(
        quizId: Long?,
        category: Category,
        difficult: Difficult
    ): Flow<Result<QuizEntity>> = repository.getExistedOrNewQuiz(quizId, category, difficult)
}