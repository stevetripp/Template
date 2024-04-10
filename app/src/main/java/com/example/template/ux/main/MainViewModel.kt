package com.example.template.ux.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.template.util.SmtLogger
import com.example.template.ux.nesteddeeplink.Level1Route
import org.lds.mobile.ext.enumValueOfOrNull
import org.lds.mobile.navigation.NavRoute
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl

class MainViewModel : ViewModel(), ViewModelNav by ViewModelNavImpl() {
    fun isReady(): Boolean = true

    fun navigate(uri: Uri?) {
        uri?.path?.substring(1)?.let { path ->
            val screen = enumValueOfOrNull<Screen>(path.uppercase()) ?: return
            SmtLogger.i(
                """uri: $uri
                |$screen
            """.trimMargin()
            )

            when (screen) {
                Screen.LEVEL_TWO,
                Screen.LEVEL_THREE -> navigate(Level1Route.createRoute(NavRoute(screen.name)))
                else -> navigate(NavRoute(screen.name))
            }
        }
    }
}