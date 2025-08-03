package ru.d3rvich.network

import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficult
import ru.d3rvich.network.model.Question
import ru.d3rvich.network.result.NetworkResult

interface DailyQuizNetworkDataSource {
    suspend fun getQuiz(
        questionsCount: Int,
        category: Category,
        difficult: Difficult
    ): NetworkResult<List<Question>>
}