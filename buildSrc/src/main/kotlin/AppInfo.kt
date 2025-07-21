@Suppress("MemberVisibilityCanBePrivate")

object AppInfo {
    const val APPLICATION_ID = "com.tnt.template"

    // Manifest version information
    object Version {
        const val CODE = 1003
        val NAME = "1.0.1 ($CODE.${System.getenv("BUILD_NUMBER")})"
    }

    object AndroidSdk {
        const val MIN = 26
        const val COMPILE = 36
        const val TARGET = COMPILE
    }
}