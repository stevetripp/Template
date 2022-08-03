package com.example.template.ux.bottomnavigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.template.ux.main.Screen
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme

@Composable
fun BottomNavigationScreen(navController: NavController) {
    BottomNavigationContent(navController::popBackStack)
}

@Composable
fun BottomNavigationContent(onBack: () -> Unit = {}) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Songs", "Artists", "Playlists")

    Scaffold(topBar = { AppTopAppBar(title = Screen.BOTTOM_NAVIGATION.title, onBack = onBack) }) {
        Box(modifier = Modifier.fillMaxSize()) {
            BottomNavigation(modifier = Modifier.align(Alignment.BottomCenter)) {
                items.forEachIndexed { index, item ->
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun BottomNavigationContentPreview() {
    AppTheme { BottomNavigationContent() }
}