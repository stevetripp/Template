plugins {
    alias(libs.plugins.android.application.plugin)
    alias(libs.plugins.arturbosch.detekt.plugin)
    alias(libs.plugins.autonomousapps.dependency.analysis.plugin)
    alias(libs.plugins.dagger.hilt.plugin)
    alias(libs.plugins.kotlin.android.plugin)
    alias(libs.plugins.kotlin.compose.plugin)
    alias(libs.plugins.kotlin.serialization.plugin)
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.triplet.play.plugin)
    alias(libs.plugins.undercouch.download.plugin)
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
        viewBinding = true // needed for ActivityVideoBinding
    }

    // defined values my* in ~/.gradle/gradle.properties
    signingConfigs {
        create("uploadConfig") {
            val tntUploadKeystore: String? by project.extra
            val tntUploadKeystorePassword: String by project.extra
            val tntUploadKeyAlias: String by project.extra
            val tntUploadKeyPassword: String by project.extra

            val envSigningKeystore = System.getenv("SIGNING_KEYSTORE")
            if (tntUploadKeystore != null) {
                storeFile = File(tntUploadKeystore)
                storePassword = tntUploadKeystorePassword
                keyAlias = tntUploadKeyAlias
                keyPassword = tntUploadKeyPassword
            } else if (envSigningKeystore != null) {
                // From environment (local or Github Actions)
                storeFile = file(envSigningKeystore)
                storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
            }
        }

        create("prodConfig") {
            val tntKeystore: String? by project.extra
            val tntKeystorePassword: String by project.extra
            val tntKeyAlias: String by project.extra
            val tntKeyPassword: String by project.extra

            val envSigningKeystore = System.getenv("SIGNING_KEYSTORE")
            if (tntKeystore != null) {
                storeFile = File(tntKeystore)
                storePassword = tntKeystorePassword
                keyAlias = tntKeyAlias
                keyPassword = tntKeyPassword
            } else if (envSigningKeystore != null) {
                // From environment (local or Github Actions)
                storeFile = file(envSigningKeystore)
                storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
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
        val alpha by creating {
            initWith(release)
            versionNameSuffix = " ALPHA"
            applicationIdSuffix = ".alpha"
        }
    }
}

dependencies {
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)

    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.compose.material3.window.size)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.media3.cast)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.hls)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.burnoutcrew.composereorderable)
    implementation(libs.coil.compose)
    implementation(libs.dagger.hilt.android)
    implementation(libs.google.android.material)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.resources)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.touchlab.kermit)
    implementation(libs.wajahatkarim.flippable)

    ksp(libs.dagger.hilt.android.compiler)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly(libs.junit.platform.launcher)
}

// ===== TEST TASKS =====

// create JUnit reports
tasks.withType<Test> {
    useJUnitPlatform()
}
// ===== Detekt =====

// download detekt config file
tasks.register<de.undercouch.gradle.tasks.download.Download>("downloadDetektConfig") {
    download {
        onlyIf { !file("build/config/detektConfig.yml").exists() }
        src("https://raw.githubusercontent.com/ICSEng/AndroidPublic/main/detekt/detektConfig-20231101.yml")
        dest("build/config/detektConfig.yml")
    }
}

// make sure when running detekt, the config file is downloaded
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    // Target version of the generated JVM bytecode. It is used for type resolution.
    this.jvmTarget = "17"
    dependsOn("downloadDetektConfig")
}

// ./gradlew detekt
// ./gradlew detektDebug
// ./gradlew detektBaselineDebug
detekt {
    allRules = true // fail build on any finding
    buildUponDefaultConfig = true // preconfigure defaults
    config.setFrom(files("$projectDir/build/config/detektConfig.yml")) // point to your custom config defining rules to run, overwriting default behavior
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


// ===== TripleT / Google Play Publisher =====
// Access can be verified with ./gradlew bootstrapListing
play {
    // try to get the credentials from gradle properties (ex: Jenkins) OR try to pull from env.ANDROID_PUBLISHER_CREDENTIALS (Gradle Actions)
    val tntServiceAccountCreds: String? by project
    tntServiceAccountCreds?.let { filename ->
        serviceAccountCredentials.set(File(filename))
    }

    track.set("internal")
    defaultToAppBundles.set(true)
}