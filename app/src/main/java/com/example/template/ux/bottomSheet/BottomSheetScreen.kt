package com.example.template.ux.bottomSheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
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
    val density = LocalDensity.current
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = SheetState(skipPartiallyExpanded = false, density = density, skipHiddenState = false))
    var showSnackbar by remember { mutableStateOf(false) }
    val peekHeight = 56.dp

    BottomSheetScaffold(
        topBar = { AppTopAppBar(title = Screen.BOTTOM_SHEET.title, onBack = onBack) },
        sheetContent = { BottomSheetContent(scaffoldState) },
        scaffoldState = scaffoldState,
        sheetPeekHeight = peekHeight,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Button(onClick = { scope.launch { scaffoldState.bottomSheetState.expand() } }) {
                Text("Show bottom sheet")
            }
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