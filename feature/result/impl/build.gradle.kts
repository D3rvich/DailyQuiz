plugins {
    alias(libs.plugins.dailyquiz.android.feature.impl)
}

android {
    namespace = "ru.d3rvich.result.impl"
}

dependencies {
    implementation(project(":feature:quiz:api"))
    implementation(project(":feature:result:api"))

    implementation(libs.androidx.material3.adaptive.navigation3)
    implementation(libs.kotlinx.serializationJson)
}