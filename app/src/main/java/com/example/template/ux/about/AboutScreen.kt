package com.example.template.ux.about

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.template.ux.MainAppScaffoldWithNavBar
import com.example.template.ux.main.Screen

@Composable
fun AboutScreen(navController: NavController) {
    MainAppScaffoldWithNavBar(title = Screen.ABOUT.title, navigationIconVisible = false) {
    }
}