plugins {
    alias(libs.plugins.dailyquiz.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ru.d3rvich.navigation"
}

dependencies {
    api(libs.androidx.navigation3.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}