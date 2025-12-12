package com.example.template.ux.about

import androidx.compose.runtime.Composable
import com.example.template.ux.MainAppScaffoldWithNavBar
import com.example.template.ux.main.Screen
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@Composable
fun AboutScreen(navigator: Navigation3Navigator) {
    MainAppScaffoldWithNavBar(
        navigator = navigator,
        title = Screen.ABOUT.title,
        navigationIconVisible = false,
        content = { },
    )
}