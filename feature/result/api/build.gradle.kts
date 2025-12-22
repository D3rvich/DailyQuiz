plugins {
    alias(libs.plugins.dailyquiz.android.feature.api)
}

android {
    namespace = "ru.d3rvich.result.api"
}

dependencies {
    implementation(libs.kotlinx.serializationJson)
}