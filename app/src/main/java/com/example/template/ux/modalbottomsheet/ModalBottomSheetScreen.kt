package com.example.template.ux.modalbottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import kotlinx.coroutines.launch

@Composable
fun ModalBottomSheetScreen(navController: NavController) {
    ModalBottomSheetContent(navController::popBackStack)
}

@Composable
private fun ModalBottomSheetContent(onBack: () -> Unit = {}) {
    var text by remember { mutableStateOf("") }
    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            // sheetContent MUST have at least one composable or an exception is thrown
            LazyColumn {
                items(50) {
                    val listItemText = "Item $it"
                    ListItem(
                        modifier = Modifier.clickable {
                            text = listItemText
                            scope.launch { state.hide() }
                        },
                        text = { Text(listItemText) },
                        icon = {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "Localized description"
                            )
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(topBar = { AppTopAppBar(title = Screen.MODAL_BOTTOM_SHEET.title, onBack = onBack) }) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Rest of the UI")
                Text(text)
                Spacer(Modifier.height(20.dp))
                Button(onClick = { scope.launch { state.show() } }) {
                    Text("Click to show sheet")
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun ModalBottomSheetContentPreview() {
    AppTheme { ModalBottomSheetContent() }
}