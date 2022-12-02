@Suppress("MemberVisibilityCanBePrivate")

object AppInfo {
    const val APPLICATION_ID = "com.tnt.template"

    // Manifest version information
    object Version {
        const val APP_NAME = "template"
        const val MIN = 1001
        val CODE = VersionCode.readVersionCode(APP_NAME, MIN)
        val NAME = "1.0.0 ($CODE.${System.getenv("BUILD_NUMBER")})"
    }

    object AndroidSdk {
        const val MIN = 23
        const val COMPILE = 33
        const val TARGET = COMPILE
    }
}