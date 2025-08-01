package ru.d3rvich.domain.usecases

import ru.d3rvich.domain.entities.SampleEntity
import ru.d3rvich.domain.repositories.SampleRepository
import javax.inject.Inject

class GetSampleUseCase @Inject constructor(private val repository: SampleRepository) {
    suspend operator fun invoke(id: Int): SampleEntity = repository.getSampleBy(id)
}