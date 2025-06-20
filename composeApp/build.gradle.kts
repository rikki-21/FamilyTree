import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    id("com.google.gms.google-services")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.material3.android)
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.firebase.firestore)
            implementation(libs.firebase.auth)
//            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose.v290)
            implementation(libs.navigation.compose)
//            implementation(libs.androidx.navigation.compose.jvmstubs)
            implementation(libs.kotlinx.coroutines.play.services)
        }
//        iosArm64Main.dependencies {
//            implementation(compose.runtime)
//            implementation(compose.foundation)
//            implementation(compose.ui)
//            implementation(libs.androidx.lifecycle.viewmodel)
//            implementation(libs.androidx.lifecycle.runtime.compose)
//            implementation(libs.kotlinx.serialization.json)
//            implementation(libs.kotlinx.datetime)
//            implementation(libs.firebase.firestore)
//            implementation(libs.firebase.auth)
//            implementation(libs.androidx.lifecycle.viewmodel.compose)
//            implementation(libs.androidx.lifecycle.viewmodel.compose.v290)
//            implementation(libs.navigation.compose)
//            implementation(libs.androidx.navigation.compose.jvmstubs)
//            implementation(libs.kotlinx.coroutines.play.services)
//        }
    }
}

android {
    namespace = "org.kollarappa.familytree"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.kollarappa.familytree"
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
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugarJdkLibs)
    implementation(libs.play.services.tasks)
    debugImplementation(compose.uiTooling)
}