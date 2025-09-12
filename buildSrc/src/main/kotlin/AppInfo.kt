@Suppress("MemberVisibilityCanBePrivate")

object AppInfo {
    const val APPLICATION_ID = "com.tnt.template"

    // Manifest version information
    object Version {
        const val CODE = 1003 // Used for local builds
        const val NAME_PREFIX = "1.0.2"
    }

    object AndroidSdk {
        const val MIN = 26
        const val COMPILE = 36
        const val TARGET = COMPILE
    }
}