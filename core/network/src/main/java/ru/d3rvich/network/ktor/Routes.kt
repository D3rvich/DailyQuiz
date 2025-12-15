package ru.d3rvich.network.ktor

import io.ktor.resources.Resource
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName

@OptIn(InternalSerializationApi::class)
@Suppress("Unused")
internal object Routes {

    @Resource("api.php")
    class Quiz(
        @SerialName("amount") val questionsCount: Int,
        @SerialName("category") val category: String? = null,
        @SerialName("difficulty") val difficulty: String? = null
    )
}