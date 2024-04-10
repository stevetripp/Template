package com.example.template.ux.nesteddeeplink

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ux.main.Screen
import org.lds.mobile.ui.compose.navigation.HandleNavigation

@Composable
fun Level1Screen(navController: NavController, viewModel: Level1ViewModel = hiltViewModel()) {
    HandleNavigation(viewModel, navController)
    Scaffold(topBar = { AppTopAppBar(title = Screen.LEVEL_ONE.title, onBack = navController::popBackStack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Button(onClick = viewModel::onLevelTwoClicked) {
                Text(text = "Level 2")
            }
            Button(onClick = viewModel::onLevelThreeClicked) {
                Text(text = "Level 3")
            }
        }
    }
}