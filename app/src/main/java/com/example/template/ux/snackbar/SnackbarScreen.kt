package com.example.template.ux.snackbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.template.ux.main.Screen
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun SnackbarScreen(navController: NavController) {
    SnackbarContent(navController::popBackStack)
}

@Composable
fun SnackbarContent(onBack: () -> Unit = {}) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = { AppTopAppBar(title = Screen.SNACKBAR.title, onBack = onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        Button(modifier = Modifier.padding(it),
            onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar("This is the text of the snackbar")
                }
            }) {
            Text("Show Snackbar")
        }
    }
}

@Preview
@Composable
private fun SnackbarContentPreview() {
    AppTheme { SnackbarContent() }
}