package com.example.template.ux.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.template.ux.NavGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavGraph(navController)
}
