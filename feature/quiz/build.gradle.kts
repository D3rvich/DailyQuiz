plugins {
    alias(libs.plugins.dailyquiz.android.feature)
}

android {
    namespace = "ru.d3rvich.quiz"
}

dependencies {
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.compose.navigation)
}