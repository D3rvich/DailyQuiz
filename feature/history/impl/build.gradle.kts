import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.dailyquiz.android.feature.impl)
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
    implementation(project(":feature:history:api"))
    implementation(project(":feature:quiz:api"))
    implementation(project(":feature:result:api"))

    implementation(libs.kotlinx.serializationJson)
    implementation(libs.androidx.material3.adaptive)
    implementation(libs.androidx.material3.adaptive.navigation3)
}