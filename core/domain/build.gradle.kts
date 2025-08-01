plugins {
    alias(libs.plugins.dailyquiz.jvm.library)
    alias(libs.plugins.ksp)
}
dependencies {
    implementation(project(":core:common"))
    api(libs.kotlinx.datetime)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
