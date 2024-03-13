plugins {
    id("com.android.application")
    kotlin("android")
    id("dagger.hilt.android.plugin")
    id("de.undercouch.download") version libs.versions.undercouchDownload
    alias(libs.plugins.detekt)
    alias(libs.plugins.ksp)
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
            // https://developer.android.com/build/shrink-code#enable
            isMinifyEnabled = true
            isShrinkResources = true
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
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-opt-in=androidx.media3.common.util.UnstableApi",
            "-opt-in=com.google.accompanist.permissions.ExperimentalPermissionsApi",
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
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.splashscreen)

    implementation(libs.coil)
    implementation(libs.coil.compose)

    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.iconsext)
    implementation(libs.compose.ui.tooling)

    implementation(libs.flippable)

    // Inject
    implementation(libs.google.hilt.android)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.media3.exoplayer)
    ksp(libs.google.hilt.android.compiler)

    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.google.material)

    implementation(libs.reorderable.compose)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)

    androidTestImplementation(libs.androidx.test.junit)
}

// ===== Detekt =====
// ./gradlew detektDebug
// ./gradlew detektBaselineDebug
detekt {
    allRules = true // fail build on any finding
    buildUponDefaultConfig = true // preconfigure defaults
    config = files("$projectDir/build/config/detektConfig.yml") // point to your custom config defining rules to run, overwriting default behavior
//    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    // ignore ImageVector files
    exclude("**/ui/compose/icons**")

    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
        txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
    }
}

tasks {
    // make sure when running detekt, the config file is downloaded
    withType<io.gitlab.arturbosch.detekt.Detekt> {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        this.jvmTarget = "17"
        dependsOn("downloadDetektConfig")
    }
}

// download detekt config file
tasks.register<de.undercouch.gradle.tasks.download.Download>("downloadDetektConfig") {
    download {
        onlyIf { !file("build/config/detektConfig.yml").exists() }
        src("https://raw.githubusercontent.com/ICSEng/AndroidPublic/main/detekt/detektConfig-20230728.yml")
        dest("build/config/detektConfig.yml")
    }
}

// create JUnit reports
tasks.withType<Test> {
    useJUnitPlatform()
}