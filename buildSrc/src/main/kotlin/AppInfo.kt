@Suppress("MemberVisibilityCanBePrivate")

object AppInfo {
    const val APPLICATION_ID = "com.tnt.template"

    // Manifest version information
    object Version {
        const val MIN = 1003 // Specify the Min version here and in the .github/workflows/release.yml file
        private const val SEMANTIC_NAME = "1.0.2"
        val CODE = System.getenv("VERSION_CODE")?.toIntOrNull() ?: MIN
        val NAME = """$SEMANTIC_NAME-($CODE.${System.getenv("CI_BUILD_NUMBER").orEmpty()})"""
    }

    object AndroidSdk {
        const val MIN = 26
        const val COMPILE = 36
        const val TARGET = COMPILE
    }
}