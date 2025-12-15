package ru.d3rvich.network.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import ru.d3rvich.domain.model.Category
import ru.d3rvich.domain.model.Difficulty
import ru.d3rvich.network.DailyQuizNetworkDataSource
import ru.d3rvich.network.model.Question
import ru.d3rvich.network.model.Quiz
import ru.d3rvich.network.result.NetworkResult
import ru.d3rvich.network.utils.safeApiCall

internal class DailyQuizNetworkClient(private val client: HttpClient) : DailyQuizNetworkDataSource {
    override suspend fun getQuiz(
        questionsCount: Int,
        category: Category,
        difficulty: Difficulty
    ): NetworkResult<List<Question>> = safeApiCall {
        val response = client.get(
            Routes.Quiz(
                questionsCount = questionsCount,
                category = if (category != Category.AnyCategory) category.code else null,
                difficulty = if (difficulty != Difficulty.AnyDifficulty) difficulty.code else null
            )
        )
        response.body<Quiz>().questions
    }
}