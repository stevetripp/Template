package com.example.template.ui.screen

import androidx.activity.compose.BackHandler
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
import com.example.template.AppBar
import com.example.template.Nav

@Composable
fun Snackbar(nav: Nav, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    val scaffoldState = rememberScaffoldState()
    var showSnackbar by remember { mutableStateOf(false) }
    Scaffold(
        topBar = { AppBar(nav, onBack) },
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