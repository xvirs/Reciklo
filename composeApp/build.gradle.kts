import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.crashlytics)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.compose.bom))
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.viewmodel.compose)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.koin.androidx.compose)
            implementation(libs.coil.compose)
            implementation(libs.lottie)
            implementation("androidx.compose.material3:material3:1.1.2")
            implementation("androidx.compose.material3:material3-window-size-class:1.1.2")
            implementation("androidx.compose.material:material-icons-core:1.3.1")
            implementation("androidx.compose.material:material-icons-extended:1.3.1")
            implementation("com.journeyapps:zxing-android-embedded:4.3.0")
            val camerax_version = "1.1.0-beta01"
            implementation ("androidx.camera:camera-core:${camerax_version}")
            implementation ("androidx.camera:camera-camera2:${camerax_version}")
            implementation ("androidx.camera:camera-lifecycle:${camerax_version}")
            implementation ("androidx.camera:camera-video:${camerax_version}")
            implementation ("androidx.camera:camera-view:${camerax_version}")
            implementation ("androidx.camera:camera-extensions:${camerax_version}")
            implementation(project.dependencies.platform(libs.firebase.bom.v3220))
            implementation(libs.firebase.crashlytics.ktx) // Crashlytics KTX
            implementation(libs.firebase.analytics.ktx) // Analytics KTX
        }
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
        }
    }
}

android {
    namespace = "com.jetbrains.kmpapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.jetbrains.kmpapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.6.4"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}
