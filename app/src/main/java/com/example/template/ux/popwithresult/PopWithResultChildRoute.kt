package com.example.template.ux.popwithresult

import kotlinx.serialization.Serializable
import org.lds.mobile.navigation.NavigationRoute

@Serializable
object PopWithResultChildRoute : NavigationRoute {
    object Arg {
        const val RESULT_STRING = "RESULT_STRING"
    }
}