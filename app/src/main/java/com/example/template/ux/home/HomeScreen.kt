package com.example.template.ux.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ux.main.Screen
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.navigation.HandleNavigation
import com.example.template.ui.theme.AppTheme

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    HomeContent(viewModel.uiState)
    HandleNavigation(viewModelNav = viewModel, navController = navController)
}

@Composable
fun HomeContent(uiState: HomeScreenUiState) {
    val scrollState = rememberScrollState()
    Scaffold(topBar = { AppTopAppBar(title = Screen.HOME.title, navigationImage = null/*, onBack = onBack*/) }) { paddingValues ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Screen.values().filterNot { it == Screen.HOME }.forEach { destination ->
                TextButton(
                    onClick = { uiState.onItemClicked(destination) },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = ButtonDefaults.textButtonColors(backgroundColor = MaterialTheme.colors.surface)
                ) {
                    Text(destination.title)
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun HomeContentPreview() {
    AppTheme { HomeContent(HomeScreenUiState()) }
}