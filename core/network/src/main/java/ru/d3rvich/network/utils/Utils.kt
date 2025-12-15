package ru.d3rvich.network.utils

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import ru.d3rvich.network.result.NetworkResult

internal suspend fun <T : Any> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> = try {
    NetworkResult.Success(apiCall())
} catch (cause: RedirectResponseException) {
    NetworkResult.Failure(cause)
} catch (cause: ClientRequestException) {
    NetworkResult.Failure(cause)
} catch (cause: ServerResponseException) {
    NetworkResult.Failure(cause)
}