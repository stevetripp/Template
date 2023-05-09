package com.example.template.ux.bottomSheet

import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.Button
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import kotlinx.coroutines.launch

@Composable
fun BottomSheetScreen(navController: NavController) {
    BottomSheetScreenContent(navController::popBackStack)
}

@Composable
fun BottomSheetScreenContent(onBack: () -> Unit = {}) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var showSnackbar by remember { mutableStateOf(false) }
    val peekHeight = 56.dp

    BottomSheetScaffold(
        topBar = { AppTopAppBar(title = Screen.BOTTOM_SHEET.title, onBack = onBack) },
        sheetContent = { BottomSheetContent(scaffoldState) },
        scaffoldState = scaffoldState,
        sheetPeekHeight = peekHeight,
        floatingActionButton = {
            FloatingActionButton(onClick = { showSnackbar = true }) {
                Icon(Icons.Default.Favorite, contentDescription = "Localized description")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        Button(onClick = { scope.launch { scaffoldState.bottomSheetState.expand() } }) {
            Text("Show bottom sheet")
        }
    }
    LaunchedEffect(key1 = showSnackbar) {
        if (showSnackbar) {
            scaffoldState.snackbarHostState.showSnackbar("Floating action button clicked.")
            showSnackbar = false
        }
    }
}

@PreviewDefault
@Composable
private fun BottomSheetScreenContentPreview() {
    AppTheme { BottomSheetScreenContent() }
}