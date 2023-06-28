package com.example.template.ux.modalbottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ui.widget.RadioButtonAndText
import com.example.template.ux.main.Screen
import kotlinx.coroutines.launch

@Composable
fun ModalBottomSheetScreen(navController: NavController) {
    ModalBottomSheetContent(navController::popBackStack)
}

@Composable
private fun ModalBottomSheetContent(onBack: () -> Unit = {}) {
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var text by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var radioButtonsData by remember {
        mutableStateOf(listOf("One Item" to 1, "Ten Items" to 10, "One Hundred Items" to 100).map { RadioButtonData(it.first, it.second, it.first == "One Item") })
    }

    if (showBottomSheet) {
        val selectedRbData = radioButtonsData.find { it.isSelected } ?: radioButtonsData[0]
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = { showBottomSheet = false }) {
            // sheetContent MUST have at least one composable or an exception is thrown
            LazyColumn {
                items(selectedRbData.itemCount) {
                    val listItemText = "Item $it"
                    ListItem(
                        modifier = Modifier.clickable {
                            text = listItemText
                            scope.launch {
                                bottomSheetState.hide() // Added for animation
                                showBottomSheet = false
                            }
                        },
                        headlineContent = { Text(listItemText) },
                        leadingContent = {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "Localized description"
                            )
                        }
                    )
                }
            }
        }
    }
    Scaffold(topBar = { AppTopAppBar(title = Screen.MODAL_BOTTOM_SHEET.title, onBack = onBack) }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            items(radioButtonsData) { rbData ->
                RadioButtonAndText(
                    text = rbData.text,
                    selected = rbData.isSelected,
                    onClick = {
                        showBottomSheet = true
                        radioButtonsData = radioButtonsData.map { it.copy(isSelected = it.text == rbData.text) }
                    }
                )
            }
        }
    }
}

@PreviewDefault
@Composable
private fun ModalBottomSheetContentPreview() {
    AppTheme { ModalBottomSheetContent() }
}

private data class RadioButtonData(val text: String, val itemCount: Int, val isSelected: Boolean = false)