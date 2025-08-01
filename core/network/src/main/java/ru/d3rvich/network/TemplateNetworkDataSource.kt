package ru.d3rvich.network

interface TemplateNetworkDataSource {
    fun sampleApiCall(): Result<String>
}