package com.example.template.ui.screen

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.template.AppBar
import com.example.template.Nav
import com.example.template.ui.theme.AppTheme

@Composable
fun SnackbarScreen(nav: Nav, onBack: () -> Unit) {
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

@Preview(group = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true)
@Preview(group = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true)
@Composable
private fun SnackbarScreenPreview() {
    AppTheme { SnackbarScreen(nav = Nav.SNACKBAR) {} }
}