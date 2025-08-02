plugins {
    alias(libs.plugins.dailyquiz.android.library)
    alias(libs.plugins.dailyquiz.android.hilt)
}

android {
    namespace = "ru.d3rvich.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
}