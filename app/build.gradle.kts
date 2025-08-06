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
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":feature:quiz"))
    implementation(project(":feature:history"))
    implementation(project(":feature:result"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.kotlinx.serializationJson)
}