plugins {
    alias(libs.plugins.dailyquiz.android.library)
    alias(libs.plugins.dailyquiz.android.hilt)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.d3rvich.database"
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.kotlinx.serializationJson)
    implementation(libs.kotlinx.datetime)
}