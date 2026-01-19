plugins {
    alias(libs.plugins.dailyquiz.android.library.compose)
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