package ru.d3rvich.domain.repositories

import ru.d3rvich.domain.entities.SampleEntity

interface SampleRepository {
    suspend fun getList(): List<SampleEntity>

    suspend fun getSampleBy(id: Int): SampleEntity
}