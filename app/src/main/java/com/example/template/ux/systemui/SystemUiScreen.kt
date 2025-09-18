package com.example.template.ux.systemui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen

@Composable
fun SystemUiScreen(navController: NavController) {
    SystemUiContent(onBack = navController::popBackStack)
}

@Composable
private fun SystemUiContent(onBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = Screen.SYSTEM_UI.title,
                onBack = onBack,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Cyan
                )
            )
        },
        containerColor = Color.Cyan // This will color the navigation bar area
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Content goes here
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { SystemUiContent() }
}