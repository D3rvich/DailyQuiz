import com.google.devtools.ksp.gradle.KspExtension

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
    sourceSets {
        getByName("androidTest").assets.srcDir("$projectDir/schemas")
    }
    extensions.configure<KspExtension> {
        arg("room.generateKotlin", "true")
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.kotlinx.serializationJson)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.espresso.core)
}