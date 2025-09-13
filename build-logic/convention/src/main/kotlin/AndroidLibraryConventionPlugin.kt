import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import ru.d3rvich.dailyquiz.configureKotlinAndroid
import ru.d3rvich.dailyquiz.libs
import ru.d3rvich.dailyquiz.testImplementation

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                lint.targetSdk = libs.findVersion("targetSdk").get().requiredVersion.toInt()
                testOptions {
                    targetSdk = libs.findVersion("targetSdk").get().requiredVersion.toInt()
                }
                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
            }

            dependencies {
//                implementation(libs.findLibrary("timber").get())
                testImplementation(libs.findLibrary("junit").get())
            }
        }
    }
}