plugins {
    alias(libs.plugins.dailyquiz.android.feature.api)
}

android {
    namespace = "ru.d3rvich.result.api"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(libs.kotlinx.serializationJson)
}