package ru.d3rvich.network

import ru.d3rvich.network.model.Question
import ru.d3rvich.network.result.NetworkResult

interface DailyQuizNetworkDataSource {
    suspend fun getQuiz(questionsCount: Int): NetworkResult<List<Question>>
}