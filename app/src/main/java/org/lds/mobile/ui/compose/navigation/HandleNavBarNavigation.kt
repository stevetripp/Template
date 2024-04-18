package org.lds.mobile.ui.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.lds.mobile.navigation.ViewModelNavBar

@Composable
fun <T : Enum<T>> HandleNavBarNavigation(
    viewModelNavBar: ViewModelNavBar<T>,
    navController: NavController?
) {
    navController ?: return
    val navigator by viewModelNavBar.navigatorFlow.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(navigator) {
        navigator?.navigate(context, navController, viewModelNavBar)
    }
}