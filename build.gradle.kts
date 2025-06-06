import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application.plugin) apply false
    alias(libs.plugins.arturbosch.detekt.plugin) apply false
    alias(libs.plugins.autonomousapps.dependency.analysis.plugin)
    alias(libs.plugins.ben.manes.versions.plugin)
    alias(libs.plugins.dagger.hilt.plugin) apply false
    alias(libs.plugins.kotlin.android.plugin) apply false
    alias(libs.plugins.kotlin.compose.plugin) apply false
    alias(libs.plugins.kotlin.serialization.plugin) apply false
    alias(libs.plugins.ksp.plugin) apply false
    alias(libs.plugins.undercouch.download.plugin) apply false
}

allprojects {

    // ./gradlew dependencyUpdates -Drevision=release --refresh-dependencies
    // Gradle Dependency Check
    apply(plugin = "com.github.ben-manes.versions") // ./gradlew dependencyUpdates -Drevision=release
//    val excludeVersionContaining = emptyList<String>()
    val excludeVersionContaining = listOf("alpha", "eap", "M1", "dev") // example: "alpha", "beta"
    // some artifacts may be OK to check for "alpha"... add these exceptions here
    val ignoreArtifacts = emptyList<String>()

    tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
        resolutionStrategy {
            componentSelection {
                all {
                    if (ignoreArtifacts.contains(candidate.module).not()) {
                        val rejected = excludeVersionContaining.any { qualifier ->
                            candidate.version.matches(Regex("(?i).*[.-]$qualifier[.\\d-+]*"))
                        }
                        if (rejected) {
                            reject("Release candidate")
                        }
                    }
                }
            }
        }
    }
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
            onUnusedAnnotationProcessors {
                exclude(
                    depGroupAndName(libs.dagger.hilt.android.compiler),
                )
            }
        }
    }
}

fun depGroupAndName(dependency: Provider<MinimalExternalModuleDependency>): String {
    return dependency.get().let { "${it.group}:${it.name}" }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}