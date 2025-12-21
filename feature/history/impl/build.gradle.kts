import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.dailyquiz.android.feature)
}

android {
    namespace = "ru.d3rvich.history.impl"
}
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

dependencies {
    implementation(libs.androidx.material3.adaptive)
}