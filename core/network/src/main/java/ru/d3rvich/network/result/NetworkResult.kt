package ru.d3rvich.network.result

sealed interface NetworkResult<out T : Any> {
    data class Success<T : Any>(val value: T) : NetworkResult<T>
    data class Failure(val exception: Throwable) : NetworkResult<Nothing>
}