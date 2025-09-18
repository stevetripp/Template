@Suppress("MemberVisibilityCanBePrivate")

object AppInfo {
    const val APPLICATION_ID = "com.tnt.template"

    // Manifest version information
    object Version {
        private const val SEMANTIC_VERSION = "1.0.2"
        val CODE = System.getenv("VERSION_CODE")?.toIntOrNull() ?: 1
        val BUILD_NUMBER = System.getenv("BUILD_NUMBER") ?: "0"
        val NAME = """$SEMANTIC_VERSION-($CODE.$BUILD_NUMBER)"""
    }

    object AndroidSdk {
        const val MIN = 26
        const val COMPILE = 36
        const val TARGET = COMPILE
    }
}