package org.lds.mobile.ui.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.navigate

@Composable
fun HandleNavigation(
    viewModelNav: ViewModelNav,
    navController: NavController?
) {
    navController ?: return
    val navigationActionState = viewModelNav.navigationActionFlow.collectAsStateWithLifecycle()
    val navigationAction = navigationActionState.value

    val context = LocalContext.current
    LaunchedEffect(navigationAction) {
        navigationAction?.navigate(context, navController, viewModelNav::resetNavigate)
    }
}
