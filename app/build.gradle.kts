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
    implementation(project(":core:navigation"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":feature:quiz:impl"))
    implementation(project(":feature:history:impl"))
    implementation(project(":feature:result"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.serializationJson)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive)
    implementation(libs.androidx.material3.adaptive.navigation3)
}