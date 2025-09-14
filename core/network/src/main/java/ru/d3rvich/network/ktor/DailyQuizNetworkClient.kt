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

internal class DailyQuizNetworkClient(private val client: HttpClient) :
    DailyQuizNetworkDataSource {
    override suspend fun getQuiz(
        questionsCount: Int,
        category: Category,
        difficulty: Difficulty
    ): NetworkResult<List<Question>> = try {
        val response = client.get(
            Routes.Quiz(
                questionsCount = questionsCount,
                category = if (category != Category.AnyCategory) category.code else null,
                difficulty = if (difficulty != Difficulty.AnyDifficulty) difficulty.code else null
            )
        )
        when (response.status.value) {
            in 200..299 -> {
                NetworkResult.Success(response.body<Quiz>().questions)
            }

            in 400..499 -> {
                NetworkResult.Failure(Exception("4xx error"))
            }

            in 500..599 -> {
                NetworkResult.Failure(Exception("Server error"))
            }

            else -> {
                NetworkResult.Failure(Exception("Unknown error"))
            }
        }

    } catch (e: Exception) {
        NetworkResult.Failure(e)
    }
}