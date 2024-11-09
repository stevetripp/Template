plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.detekt)
    alias(libs.plugins.download)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi",
            "-opt-in=com.google.accompanist.permissions.ExperimentalPermissionsApi",
        )
    }
    buildFeatures {
        buildConfig = true
        compose = true
        viewBinding = true
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // set dummy signing values if not defined in ~/.gradle/gradle.properties
    if (!project.hasProperty("tntKeystore")) {
        println("Using dummy signing values")
        project.extra.set("tntKeystore", "dummy")
        project.extra.set("tntKeystorePassword", "dummy")
        project.extra.set("tntKeyAlias", "dummy")
        project.extra.set("tntKeyPassword", "dummy")
    }
    // set dummy signing values if not defined in ~/.gradle/gradle.properties
    if (!project.hasProperty("tntUploadKeystore")) {
        println("Using dummy signing values")
        project.extra.set("tntUploadKeystore", "dummy")
        project.extra.set("tntUploadKeystorePassword", "dummy")
        project.extra.set("tntUploadKeyAlias", "dummy")
        project.extra.set("tntUploadKeyPassword", "dummy")
    }

    // defined values my* in ~/.gradle/gradle.properties
    signingConfigs {
        create("uploadConfig") {
            val tntUploadKeystore: String? by project.extra
            val tntUploadKeystorePassword: String by project.extra
            val tntUploadKeyAlias: String by project.extra
            val tntUploadKeyPassword: String by project.extra

            tntUploadKeystore?.let {
                storeFile = File(it)
                storePassword = tntUploadKeystorePassword
                keyAlias = tntUploadKeyAlias
                keyPassword = tntUploadKeyPassword
            }
        }

        create("prodConfig") {
            val tntKeystore: String? by project.extra
            val tntKeystorePassword: String by project.extra
            val tntKeyAlias: String by project.extra
            val tntKeyPassword: String by project.extra

            tntKeystore?.let {
                storeFile = File(it)
                storePassword = tntKeystorePassword
                keyAlias = tntKeyAlias
                keyPassword = tntKeyPassword
            }
        }
    }

    buildTypes {
        val debug by getting {
            versionNameSuffix = "-DEV"
            applicationIdSuffix = ".dev"
            signingConfig = signingConfigs.getByName("uploadConfig")
            resValue("string", "file_provider", "com.tnt.template.dev.fileprovider")
        }
        val release by getting {
            signingConfig = signingConfigs.getByName("uploadConfig")

            // https://developer.android.com/build/shrink-code#enable
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            resValue("string", "file_provider", "com.tnt.template.fileprovider")
        }
    }
}

dependencies {

    androidTestImplementation(libs.androidx.test.junit)

    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.biometrics)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.media3.cast)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.splashscreen)
    implementation(libs.coil.compose)
    implementation(libs.compose.material)
    implementation(libs.compose.material.iconsext)
    implementation(libs.compose.material3)
    implementation(libs.compose.material3.adaptive)
    implementation(libs.compose.material3.adaptive.navigation)
    implementation(libs.compose.material3.windowsize)
    implementation(libs.compose.ui.tooling)
    implementation(libs.flippable)
    implementation(libs.google.hilt.android)
    implementation(libs.google.material)
    implementation(libs.kermit)
    implementation(libs.kotlin.date.time)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.resources)
    implementation(libs.ktor.client.serialization)
    implementation(libs.reorderable.compose)

    ksp(libs.google.hilt.android.compiler)

    testImplementation(libs.junit.jupiter)
    testImplementation(platform(libs.junit.bom))
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
        src("https://raw.githubusercontent.com/ICSEng/AndroidPublic/main/detekt/detektConfig-20231101.yml")
        dest("build/config/detektConfig.yml")
    }
}

// create JUnit reports
tasks.withType<Test> {
    useJUnitPlatform()
}