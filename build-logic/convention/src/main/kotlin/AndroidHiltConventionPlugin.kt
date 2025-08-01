import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import ru.d3rvich.dailyquiz.implementation
import ru.d3rvich.dailyquiz.ksp
import ru.d3rvich.dailyquiz.libs

class AndroidHiltConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.google.devtools.ksp")
                apply("com.google.dagger.hilt.android")
            }
            dependencies {
                implementation(libs.findLibrary("androidx-hilt-android").get())
                ksp(libs.findLibrary("androidx-hilt-compiler").get())
            }
        }
    }
}