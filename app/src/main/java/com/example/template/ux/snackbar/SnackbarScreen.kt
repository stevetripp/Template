package com.example.template.ux.snackbar

import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.template.ux.main.Screen
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme

@Composable
fun SnackbarScreen(navController: NavController) {
    SnackbarContent(navController::popBackStack)
}

@Composable
fun SnackbarContent(onBack: () -> Unit = {}) {
    val scaffoldState = rememberScaffoldState()
    var showSnackbar by remember { mutableStateOf(false) }
    Scaffold(
        topBar = { AppTopAppBar(title = Screen.SNACKBAR.title, onBack = onBack) },
        scaffoldState = scaffoldState
    ) {
        Button(onClick = { showSnackbar = true }) {
            Text("Show Snackbar")
        }
    }
    LaunchedEffect(key1 = showSnackbar) {
        if (showSnackbar) {
            scaffoldState.snackbarHostState.showSnackbar("This is the text of the snackbar")
            showSnackbar = false
        }
    }
}

@Preview
@Composable
private fun SnackbarContentPreview() {
    AppTheme { SnackbarContent() }
}