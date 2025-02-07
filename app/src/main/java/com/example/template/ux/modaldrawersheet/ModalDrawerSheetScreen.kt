package com.example.template.ux.modaldrawersheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppModalDrawerSheet
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import kotlinx.coroutines.launch

@Composable
fun ModelDrawerSheetScreen(navController: NavController) {
    ModelDrawerSheetContent(navController::popBackStack)
}

@Composable
fun ModelDrawerSheetContent(onBack: () -> Unit = {}) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var layoutDirection by remember { mutableStateOf(LayoutDirection.Ltr) }

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = Screen.MODAL_DRAWER_SHEET.title,
                onBack = onBack,
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            layoutDirection = LayoutDirection.Ltr
                            drawerState.open()
                        }
                    }) { Icon(Icons.Default.ChevronRight, contentDescription = null) }
                    IconButton(onClick = {
                        scope.launch {
                            layoutDirection = LayoutDirection.Rtl
                            drawerState.open()
                        }
                    }) { Icon(Icons.Default.ChevronLeft, contentDescription = null) }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ListItem(headlineContent = { Text(text = "Click chevron to open Model Drawer Sheet") })
        }
    }

    AppModalDrawerSheet(
        drawerState = drawerState,
        onDismissRequest = { scope.launch { drawerState.close() } },
        layoutDirection
    ) {
        Scaffold(
            topBar = {
                AppTopAppBar(
                    title = "Modal Drawer Sheet",
                    onBack = { scope.launch { drawerState.close() } },
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                ListItem(headlineContent = { Text(text = "Modal Drawer Sheet") })
            }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { ModelDrawerSheetContent() }
}