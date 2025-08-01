plugins {
    alias(libs.plugins.dailyquiz.jvm.library)
    alias(libs.plugins.ksp)
}
dependencies {
    implementation(project(":core:common"))
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
