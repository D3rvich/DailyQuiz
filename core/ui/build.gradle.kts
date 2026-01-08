plugins {
    alias(libs.plugins.dailyquiz.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.d3rvich.ui"
}

dependencies {
    implementation(project(":core:domain"))

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.kotlinx.collections.immutable)
}