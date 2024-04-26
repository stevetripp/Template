package com.example.template.ux.childwithnavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.template.ux.MainAppScaffoldWithNavBar
import com.example.template.ux.main.Screen

@Composable
fun ChildWithNavigationScreen(navController: NavController) {
    MainAppScaffoldWithNavBar(
        title = Screen.CHILD_WITH_NAVIGATION.title, onNavigationClick = navController::popBackStack
    ) {

    }
}