@Suppress("MemberVisibilityCanBePrivate")

object AppInfo {
    const val APPLICATION_ID = "com.tnt.template"

    // Manifest version information
    object Version {
        const val APP_NAME = "template"
        const val MIN = 1002
        val CODE = System.getenv("VERSION_CODE")?.toIntOrNull() ?: MIN
        val NAME = "1.0.0 ($CODE.${System.getenv("BUILD_NUMBER")})"
    }

    object AndroidSdk {
        const val MIN = 26
        const val COMPILE = 34
        const val TARGET = COMPILE
    }
}