package ru.d3rvich.dailyquiz

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(commonExtension: CommonExtension) {
    commonExtension.apply {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        buildFeatures.apply {
            compose = true
            buildConfig = true
        }
        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            "implementation"(platform(bom))
            "implementation"(libs.findLibrary("androidx-compose-ui-core").get())
            "implementation"(libs.findLibrary("androidx-compose-ui-graphics").get())
            "implementation"(libs.findLibrary("androidx-compose-material3").get())
            "implementation"(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            "androidTestImplementation"(platform(bom))
            "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
            "debugImplementation"(libs.findLibrary("androidx-compose-ui-test-manifest").get())
        }
    }
}