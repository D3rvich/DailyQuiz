plugins {
    alias(libs.plugins.dailyquiz.android.library)
    alias(libs.plugins.dailyquiz.android.hilt)
}

android {
    namespace = "ru.d3rvich.network"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.resources)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.serialization.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.logging.slf4j)
}