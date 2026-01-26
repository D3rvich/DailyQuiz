plugins {
    alias(libs.plugins.dailyquiz.android.library)
    alias(libs.plugins.dailyquiz.android.hilt)
    alias(libs.plugins.dailyquiz.android.room)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.d3rvich.database"
    sourceSets {
        getByName("androidTest").assets.directories.add("$projectDir/schemas")
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.kotlinx.serializationJson)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.espresso.core)
}