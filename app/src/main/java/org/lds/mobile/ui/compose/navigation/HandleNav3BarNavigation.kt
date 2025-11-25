package org.lds.mobile.ui.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.lds.mobile.navigation3.ViewModelNavigation3Bar
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@Composable
fun <T : Enum<T>> HandleNav3BarNavigation(
    viewModelNavigationBar: ViewModelNavigation3Bar<T>,
    navigator: Navigation3Navigator,
) {
    val navBarNavigator by viewModelNavigationBar.navBarNavigatorFlow.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(navigator) {
        navBarNavigator?.navigate(
            context = context,
            navigator = navigator,
            viewModelNav = viewModelNavigationBar
        )
    }
}
