package ru.d3rvich.network.ktor

interface TemplateApiService {
    fun sampleApiCall(): Result<String>
}

private const val BASE_URL = "YOUR_BASE_URL"