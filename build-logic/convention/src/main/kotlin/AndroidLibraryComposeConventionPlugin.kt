import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import ru.d3rvich.dailyquiz.configureAndroidCompose
import ru.d3rvich.dailyquiz.libs

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "dailyquiz.android.library")

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)

            dependencies {
                "androidTestImplementation"(libs.findLibrary("androidx.junit").get())
                "androidTestImplementation"(libs.findLibrary("androidx.espresso.core").get())
                "androidTestImplementation"(libs.findLibrary("androidx.compose.ui.test.junit4").get())
            }
        }
    }
}