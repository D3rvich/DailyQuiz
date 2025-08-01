plugins {
    alias(libs.plugins.dailyquiz.android.application.compose)
    alias(libs.plugins.dailyquiz.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.d3rvich.dailyquiz"

    defaultConfig {
        applicationId = "ru.d3rvich.dailyquiz"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.navigation)
}