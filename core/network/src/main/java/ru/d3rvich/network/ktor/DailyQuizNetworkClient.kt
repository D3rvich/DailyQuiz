package ru.d3rvich.network.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import ru.d3rvich.common.result.Result
import ru.d3rvich.network.DailyQuizNetworkDataSource
import ru.d3rvich.network.model.Question
import ru.d3rvich.network.model.Quiz
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DailyQuizNetworkClient @Inject constructor(private val client: HttpClient) :
    DailyQuizNetworkDataSource {
    override suspend fun getQuiz(questionsCount: Int): Result<List<Question>> = try {
        val response = client.get(Routes.Quiz(questionsCount = questionsCount))
        when (response.status.value) {
            in 200..299 -> {
                Result.Success(response.body<Quiz>().questions)
            }

            in 400..499 -> {
                Result.Error(Exception("4xx error"))
            }

            in 500..599 -> {
                Result.Error(Exception("Server error"))
            }

            else -> {
                Result.Error(Exception("Unknown error"))
            }
        }

    } catch (e: Exception) {
        Result.Error(e)
    }
}