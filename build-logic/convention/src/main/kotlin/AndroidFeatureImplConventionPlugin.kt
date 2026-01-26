import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import ru.d3rvich.dailyquiz.libs

class AndroidFeatureImplConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "dailyquiz.android.library.compose")
            apply(plugin = "dailyquiz.android.hilt")
            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }
            dependencies {
                "implementation"(project(":core:domain"))
                "implementation"(project(":core:ui"))

                "implementation"(libs.findLibrary("androidx.hilt.navigation.compose").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                "implementation"(libs.findLibrary("androidx.navigation3.runtime").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.viewmodel.compose").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.viewmodel.ktx").get())
                "implementation"(libs.findLibrary("kotlinx.collections.immutable").get())
            }
        }
    }
}