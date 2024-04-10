package com.example.template.ux.nesteddeeplink

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ux.main.Screen
import org.lds.mobile.ui.compose.navigation.HandleNavigation

@Composable
fun Level3Screen(navController: NavController, viewModel: Level1ViewModel = hiltViewModel()) {
    HandleNavigation(viewModel, navController)
    Scaffold(topBar = { AppTopAppBar(title = Screen.LEVEL_THREE.title, onBack = navController::popBackStack) }) { paddingValues ->

    }
}