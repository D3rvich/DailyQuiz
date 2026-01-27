plugins {
    alias(libs.plugins.dailyquiz.android.feature.impl)
}

android {
    namespace = "ru.d3rvich.quiz.impl"
}

dependencies {
    implementation(project(":feature:quiz:api"))
    implementation(project(":feature:history:api"))
    implementation(project(":feature:result:api"))

    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.material3.adaptive)
    implementation(libs.kotlinx.datetime)
}