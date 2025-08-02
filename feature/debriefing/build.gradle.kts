plugins {
    alias(libs.plugins.dailyquiz.android.feature)
}

android {
    namespace = "ru.d3rvich.debriefing"
}

dependencies {
    implementation(libs.androidx.compose.navigation)
}