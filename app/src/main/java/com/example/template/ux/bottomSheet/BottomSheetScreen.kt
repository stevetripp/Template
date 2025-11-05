package com.example.template.ux.bottomSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipPartiallyExpanded = false,
            initialValue = SheetValue.PartiallyExpanded,
            positionalThreshold = { 0.5f },
            velocityThreshold = { 400f }
        )
    )
    val bottomNavBarHeightDp = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val peekHeight = 200.dp + bottomNavBarHeightDp

    BottomSheetScaffold(
        topBar = { AppTopAppBar(title = Screen.BOTTOM_SHEET.title, onBack = onBack) },
        sheetContent = { BottomSheetContent(onHide = { scope.launch { scaffoldState.bottomSheetState.hide() } }) },
        scaffoldState = scaffoldState,
        sheetPeekHeight = peekHeight,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Button(onClick = { scope.launch { scaffoldState.bottomSheetState.expand() } }) { Text("Expand") }
            Button(onClick = { scope.launch { scaffoldState.bottomSheetState.partialExpand() } }) { Text("Partial Expand") }
        }
    }
}

@PreviewDefault
@Composable
private fun BottomSheetScreenContentPreview() {
    AppTheme { BottomSheetScreenContent() }
}