import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import ru.d3rvich.dailyquiz.implementation
import ru.d3rvich.dailyquiz.libs

class AndroidFeatureImplConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dailyquiz.android.library.compose")
                apply("dailyquiz.android.hilt")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }
            dependencies {
                implementation(project(":core:domain"))
                implementation(project(":core:ui"))

                implementation(libs.findLibrary("androidx-hilt-navigation-compose").get())
                implementation(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
                implementation(libs.findLibrary("androidx-navigation3-runtime").get())
                implementation(libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
                implementation(libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
            }
        }
    }
}