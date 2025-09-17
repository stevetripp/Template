@Suppress("MemberVisibilityCanBePrivate")

object AppInfo {
    const val APPLICATION_ID = "com.tnt.template"

    // Manifest version information
    object Version {
        const val MIN = 1811 // Specify the Min version code here and in the .github/workflows/release.yml MIN_VERSION_CODE variable
        private const val SEMANTIC_VERSION = "1.0.2"
        val CODE = System.getenv("VERSION_CODE")?.toIntOrNull() ?: MIN
        val RUN_NUMBER = System.getenv("RUN_NUMBER").orEmpty()
        val NAME = """$SEMANTIC_VERSION-($CODE.$RUN_NUMBER)"""
    }

    object AndroidSdk {
        const val MIN = 26
        const val COMPILE = 36
        const val TARGET = COMPILE
    }
}