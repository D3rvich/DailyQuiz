package ru.d3rvich.domain.usecases

import ru.d3rvich.domain.entities.QuestionEntity
import ru.d3rvich.domain.repositories.SampleRepository
import javax.inject.Inject

class GetSampleUseCase @Inject constructor(private val repository: SampleRepository) {
    suspend operator fun invoke(id: Int): QuestionEntity = throw Exception()
}