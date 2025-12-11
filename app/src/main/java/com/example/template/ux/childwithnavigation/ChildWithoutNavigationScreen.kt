package com.example.template.ux.childwithnavigation

import androidx.compose.runtime.Composable
import com.example.template.ux.MainAppScaffoldWithNavBar
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@Composable
fun ChildWithoutNavigationScreen(navigator: Navigation3Navigator) {
    ChildWithoutNavigationContent(navigator)
}

@Composable
fun ChildWithoutNavigationContent(navigator: Navigation3Navigator) {
    MainAppScaffoldWithNavBar(
        title = "Child Without Navigation",
        selectedRoute = navigator.getSelectedTopLevelRoute(),
        onNavBarItemSelected = { navBarItem, reselected -> navigator.navigateTopLevel(navBarItem.route, reselected) },
        hideNavigation = true,
        onNavigationClick = navigator::pop
    ) {
    }
}