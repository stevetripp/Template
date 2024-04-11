package com.example.template.ux.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.template.ux.NavGraph
import org.lds.mobile.ui.compose.navigation.HandleNavigation

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    NavGraph(navController)

    HandleNavigation(viewModel, navController)
}
