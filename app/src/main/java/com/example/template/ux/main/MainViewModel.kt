package com.example.template.ux.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.template.util.SmtLogger
import org.lds.mobile.navigation.NavRoute
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl

class MainViewModel : ViewModel(), ViewModelNav by ViewModelNavImpl() {
    fun isReady(): Boolean = true

    fun navigate(uri: Uri?) {
        uri?.path?.substring(1)?.let { path ->
            SmtLogger.i(
                """uri: $uri
                |path: $path
            """.trimMargin()
            )
            if (path.isBlank()) return@let
            navigate(NavRoute(path))
        }
    }
}