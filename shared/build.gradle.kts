import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidLibrary {
        namespace = "com.example.template.shared"

        minSdk = AppInfo.AndroidSdk.MIN
        compileSdk = AppInfo.AndroidSdk.COMPILE

        withHostTestBuilder {}

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    // Host-test target only -- lets commonMain code be unit tested on the plain JVM (jvmTest)
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    compilerOptions {
        optIn.add("kotlinx.coroutines.ExperimentalCoroutinesApi")
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.androidx.datastore.preferences)
                api(libs.koin.core)
                api(libs.kotlinx.coroutines.core)
                api(libs.kotlinx.serialization.json)
                api(libs.ktor.client.core)
                api(libs.okio)
                api(libs.touchlab.kermit)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.resources)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.koin.test)
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.ktor.client.mock)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.ktor.client.java)
            }
        }
    }
}
