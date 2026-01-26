import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import ru.d3rvich.dailyquiz.configureAndroidCompose
import ru.d3rvich.dailyquiz.configureKotlinAndroid
import ru.d3rvich.dailyquiz.libs

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.apply {
                    targetSdk = libs.findVersion("targetSdk").get().requiredVersion.toInt()
                    minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
                }
                testOptions.animationsDisabled = true
                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
            }

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
            dependencies {
//                implementation(libs.findLibrary("timber").get())
                "testImplementation"(libs.findLibrary("junit").get())
                "androidTestImplementation"(libs.findLibrary("junit").get())
                "androidTestImplementation"(libs.findLibrary("androidx.espresso.core").get())
                "androidTestImplementation"(
                    libs.findLibrary("androidx.compose.ui.test.junit4").get()
                )
            }
        }
    }
}