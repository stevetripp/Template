plugins {
    id("com.android.application")
    kotlin("kapt")
    kotlin("android")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.template"

    compileSdk = AppInfo.AndroidSdk.COMPILE

    defaultConfig {
        applicationId = AppInfo.APPLICATION_ID
        minSdk = AppInfo.AndroidSdk.MIN
        targetSdk = AppInfo.AndroidSdk.TARGET
        versionCode = AppInfo.Version.CODE
        versionName = AppInfo.Version.NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        val debug by getting {
            resValue("string", "file_provider", "com.tnt.template.dev.fileprovider")
        }
        val release by getting {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue("string", "file_provider", "com.tnt.template.fileprovider")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-opt-in=com.google.accompanist.pager.ExperimentalPagerApi",
            "-opt-in=com.google.accompanist.permissions.ExperimentalPermissionsApi",
            "-opt-in=kotlin.ExperimentalStdlibApi",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        )
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.accompanist.flowlayout)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.splashscreen)

    implementation(libs.coil)
    implementation(libs.coil.compose)

    implementation(libs.compose.material)
    implementation(libs.compose.material.iconsext)
    implementation(libs.compose.ui.tooling)

    implementation(libs.flippable)

    // Inject
    implementation(libs.google.hilt.android)
    kapt(libs.google.hilt.android.compiler)

    implementation(libs.google.material)

    implementation(libs.reorderable.compose)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)

    androidTestImplementation(libs.androidx.test.junit)
}

// create JUnit reports
tasks.withType<Test> {
    useJUnitPlatform()
}