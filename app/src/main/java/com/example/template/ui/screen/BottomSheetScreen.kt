package com.example.template.ui.screen

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.template.AppBar
import com.example.template.Nav
import com.example.template.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun BottomSheetScreen(nav: Nav, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var showSnackbar by remember { mutableStateOf(false) }
    BottomSheetScaffold(
        topBar = { AppBar(nav, onBack) },
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(128.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Swipe up to expand sheet")
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sheet content")
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch { scaffoldState.bottomSheetState.collapse() }
                    }
                ) {
                    Text("Click to collapse sheet")
                }
            }
        },
        scaffoldState = scaffoldState,
        floatingActionButton = {
            var clickCount by remember { mutableStateOf(0) }
            FloatingActionButton(
                onClick = { showSnackbar = true }
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "Localized description")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        sheetPeekHeight = 0.dp,
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

@Preview(group = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true)
@Preview(group = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true)
@Composable
private fun BottomSheetScreenPreview() {
    AppTheme { BottomSheetScreen(nav = Nav.BOTTOM_SHEET, onBack = {}) }
}