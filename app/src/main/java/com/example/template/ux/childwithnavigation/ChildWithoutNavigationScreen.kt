package com.example.template.ux.childwithnavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.template.ux.MainAppScaffoldWithNavBar

@Composable
fun ChildWithoutNavigationScreen(navController: NavController) {
    ChildWithoutNavigationContent(navController::popBackStack)
}

@Composable
fun ChildWithoutNavigationContent(onBack: () -> Unit) {
    MainAppScaffoldWithNavBar(
        title = "Child Without Navigation",
        hideNavigation = true,
        onNavigationClick = onBack
    ) {

    }
}