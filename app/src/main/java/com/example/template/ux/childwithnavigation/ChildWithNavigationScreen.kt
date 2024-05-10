package com.example.template.ux.childwithnavigation

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.template.ux.MainAppScaffoldWithNavBar
import com.example.template.ux.main.Screen

@Composable
fun ChildWithNavigationScreen(navController: NavController) {

    val fab = @Composable {
        FloatingActionButton(onClick = { navController.navigate(ChildWithoutNavigationRoute.createRoute().value) }) {
            Text(text = "Click Me")
        }
    }
    MainAppScaffoldWithNavBar(
        title = Screen.CHILD_WITH_NAVIGATION.title, onNavigationClick = navController::popBackStack, floatingActionButton = fab
    ) {
        TextButton(onClick = { navController.navigate(ChildWithoutNavigationRoute.createRoute().value) }) {
            Text(text = "Click Me")
        }
    }
}