import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import ru.d3rvich.dailyquiz.api

class AndroidFeatureApiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dailyquiz.android.library")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            dependencies {
                api(project("core:navigation"))
            }
        }
    }
}