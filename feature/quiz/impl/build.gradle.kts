plugins {
    alias(libs.plugins.dailyquiz.android.feature)
}

android {
    namespace = "ru.d3rvich.quiz.impl"
}

dependencies {
    implementation(project(":feature:quiz:api"))

    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.material3.adaptive)
    implementation(libs.kotlinx.datetime)
}