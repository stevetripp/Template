import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import de.undercouch.gradle.tasks.download.Download

// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.arturbosch.detekt)
    alias(libs.plugins.autonomousapps.dependency.analysis)
    alias(libs.plugins.ben.manes.versions)
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.undercouch.download)
}

// ===== Gradle Dependency Check =====
// ./gradlew dependencyUpdates -Drevision=release
// ./gradlew dependencyUpdates -Drevision=release --refresh-dependencies
//
// ./gradlew app:dependencyInsight --configuration debugRuntimeClasspath --dependency androidx.room
// ./gradlew shared:dependencyInsight --configuration commonMainApiDependenciesMetadata --dependency androidx.room
// ./gradlew shared:resolvableConfigurations | grep "^Configuration"
tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf { isNonStable(version = candidate.version, includeStablePreRelease = true) }
}

fun isNonStable(version: String, includeStablePreRelease: Boolean): Boolean {
    val stablePreReleaseKeyword = listOf("RC", "BETA").any { version.uppercase().contains(it) }
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+$".toRegex()
    val isStable = if (includeStablePreRelease) {
        stableKeyword || regex.matches(version) || stablePreReleaseKeyword
    } else {
        stableKeyword || regex.matches(version)
    }
    return isStable.not()
}

// ===== Dependency Analysis =====
// ./gradlew projectHealth
dependencyAnalysis {
    structure {
        ignoreKtx(true)
    }
    issues {
        all {
            onAny {
                severity("fail")
            }
            onUnusedDependencies {
                exclude(
                    depGroupAndName(libs.junit.jupiter), // Needed for unit tests
                )
            }
            onUsedTransitiveDependencies { severity("ignore") }
            onIncorrectConfiguration { severity("ignore") }
            onCompileOnly { severity("ignore") }
            onRuntimeOnly { severity("ignore") }
            onUnusedAnnotationProcessors { }
        }
    }
}

fun depGroupAndName(dependency: Provider<MinimalExternalModuleDependency>): String {
    return dependency.get().let { "${it.group}:${it.name}" }
}

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}

allprojects {
    apply(plugin = "dev.detekt")
    apply(plugin = "de.undercouch.download")

    // ===== Detekt =====
    // download detekt config file
    tasks.register<Download>("downloadDetektConfig") {
        src("https://mobile-cdn.churchofjesuschrist.org/android/build/detekt/v2/detektConfig-latest.yml")
        dest("$projectDir/build/config/detektConfig.yml")
        onlyIf { !file("$projectDir/build/config/detektConfig.yml").exists() }
    }

    // ./gradlew detekt
    // ./gradlew detektDebug (support type checking)
    detekt {
        // Only analyze actual source directories, not generated code
//        source.setFrom("src/main/java", "src/main/kotlin", "src/commonMain/kotlin", "src/desktopMain/kotlin", "src/androidMain/kotlin")
        allRules = true // fail build on any finding
        buildUponDefaultConfig = true // preconfigure defaults
        config.setFrom(files("$projectDir/build/config/detektConfig.yml")) // point to your custom config defining rules to run, overwriting default behavior
    }

    tasks.withType<dev.detekt.gradle.Detekt>().configureEach {
        // Exclude generated files and ImageVector files
        exclude("**/ui/compose/icons/**")
//        exclude { it.file.absolutePath.contains("generated") } // temporary fix to exclude generated files.

        dependsOn("downloadDetektConfig")

        reports {
            html.required.set(true) // observe findings in your browser with structure and code snippets
        }
    }
}

