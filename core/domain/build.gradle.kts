plugins {
    alias(libs.plugins.dailyquiz.jvm.library)
    alias(libs.plugins.ksp)
}
dependencies {
    api(libs.kotlinx.datetime)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
