import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "1.9.10"

}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.android.sdk)
            implementation(libs.androidx.appcompat.v161)

            // Neshan Sdk library
            implementation(libs.mobile.sdk)
            implementation(libs.services.sdk)
            implementation(libs.common.sdk)

            //Play Services
            implementation(libs.play.services.gcm)
            implementation(libs.play.services.location)
            implementation(libs.androidx.constraintlayout.v220)

            // Navigation
            implementation(libs.androidx.navigation.compose)

            // Metrrial
            implementation(libs.material.v190)  // Adjust the version as needed


            //Play Services
            implementation(libs.play.services.gcm)


            // material icons
            implementation(libs.androidx.material.icons.extended)


        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.serialization)


            // Serilize
            implementation(libs.ktor.client.content.negotiation) // Use the latest version
            implementation(libs.ktor.serialization.kotlinx.json) // Serializer for JSON
            implementation(libs.kotlinx.serialization.json) // Adjust to the latest version

            // LiveData
            implementation(libs.androidx.runtime.livedata)

        }
    }
}

android {
    lint {
        checkReleaseBuilds = false
    }
    namespace = "com.reza.sampleproject"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.reza.sampleproject"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.material3.android)
    debugImplementation(compose.uiTooling)
}
