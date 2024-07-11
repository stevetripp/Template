package com.example.template.ux.systemui

import android.view.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavController
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ux.main.Screen
import org.lds.mobile.ui.ext.requireActivity

@Composable
fun SystemUiScreen(navController: NavController) {
    val window = rememberWindow()

    Scaffold(topBar = { AppTopAppBar(title = Screen.SYSTEM_UI.title, onBack = navController::navigateUp) }) {
        Column(modifier = Modifier.padding(it)) {
        }
    }

    SideEffect {
        window.statusBarColor = Color.Cyan.toArgb()
        window.navigationBarColor = window.statusBarColor
    }
}

@Composable
fun rememberWindow(): Window {
    val view = LocalView.current
    val window = view.context.requireActivity().window
    return remember(view) { window }
}
