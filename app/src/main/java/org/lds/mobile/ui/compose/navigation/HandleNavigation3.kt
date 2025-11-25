package org.lds.mobile.ui.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import navigate
import org.lds.mobile.navigation3.ViewModelNavigation3
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@Composable
fun HandleNavigation3(
    viewModelNavigation: ViewModelNavigation3,
    navigator: Navigation3Navigator
) {
    val navigationActionState = viewModelNavigation.navigationActionFlow.collectAsStateWithLifecycle()
    val navigationAction = navigationActionState.value

    val context = LocalContext.current
    LaunchedEffect(navigationAction) {
        navigationAction?.navigate(context = context, navigator = navigator, resetNavigate = viewModelNavigation::resetNavigate)
    }
}