plugins {
    alias(libs.plugins.dailyquiz.android.feature)
}

android {
    namespace = "ru.d3rvich.detail"
}

dependencies {
    implementation(libs.androidx.compose.navigation)
}