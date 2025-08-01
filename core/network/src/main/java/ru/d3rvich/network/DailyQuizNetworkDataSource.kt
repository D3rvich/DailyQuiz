package ru.d3rvich.network

import ru.d3rvich.common.result.Result
import ru.d3rvich.network.model.Question

interface DailyQuizNetworkDataSource {
    suspend fun getQuiz(questionsCount: Int): Result<List<Question>>
}