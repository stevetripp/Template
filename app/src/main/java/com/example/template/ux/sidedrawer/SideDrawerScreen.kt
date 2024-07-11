package com.example.template.ux.sidedrawer

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ux.main.Screen
import kotlinx.coroutines.launch

@Composable
fun SideDrawerScreen(navController: NavController) {
    SideDrawerContent(navController::navigateUp)
}

@Composable
private fun SideDrawerContent(onBack: () -> Unit) {
    val normalDirection = LocalLayoutDirection.current
    val reverseDirection = if (normalDirection == LayoutDirection.Ltr) LayoutDirection.Rtl else LayoutDirection.Ltr
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    CompositionLocalProvider(LocalLayoutDirection provides reverseDirection) {
        ModalNavigationDrawer(
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides normalDirection) {
                    Scaffold(topBar = { AppTopAppBar(title = Screen.SIDE_DRAWER.title, navigationImage = null) }) {
                        Text(modifier = Modifier.padding(it), text = "This is the modal drawer")
                    }
                }
            },
            drawerState = drawerState
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides normalDirection) {
                Scaffold(
                    topBar = { AppTopAppBar(title = Screen.SIDE_DRAWER.title, onBack = onBack) },
                ) { paddingValues ->
                    Text(modifier = Modifier.padding(paddingValues), text = "This is the scaffold content")
                    TextButton(onClick = { scope.launch { drawerState.open() } }) {
                        Text(text = "Open Drawer")
                    }
                }
            }
        }
    }
}