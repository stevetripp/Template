package com.example.template.ux.childwithnavigation

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.template.ux.MainAppScaffoldWithNavBar
import com.example.template.ux.main.Screen
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@Composable
fun ChildWithNavigationScreen(navigator: Navigation3Navigator) {

    val fab = @Composable {
        FloatingActionButton(onClick = { navigator.navigate(ChildWithoutNavigationRoute) }) {
            Text(text = "Click Me")
        }
    }
    MainAppScaffoldWithNavBar(
        title = Screen.CHILD_WITH_NAVIGATION.title,
        selectedRoute = navigator.getSelectedTopLevelRoute(),
        onNavBarItemSelected = { navBarItem, reselected -> navigator.navigateTopLevel(navBarItem.route, reselected) },
        onNavigationClick = navigator::pop,
        floatingActionButton = fab
    ) {
        TextButton(onClick = { navigator.navigate(ChildWithoutNavigationRoute) }) {
            Text(text = "Click Me")
        }
    }
}