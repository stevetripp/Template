package com.example.template.ux.modalbottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ui.widget.RadioButtonAndText
import com.example.template.ux.main.Screen
import kotlinx.coroutines.launch
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@Composable
fun ModalBottomSheetScreen(navigator: Navigation3Navigator) {
    ModalBottomSheetContent(navigator::pop)
}

@Composable
private fun ModalBottomSheetContent(onBack: () -> Unit = {}) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var text by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var radioButtonsData by remember {
        mutableStateOf(
            (1..30).map { "$it Item(s)" to it }.map { RadioButtonData(it.first, it.second, it.first == "One Item") })
    }
    var applyPadding by remember { mutableStateOf(true) }

    if (bottomSheetState.isVisible) {
        val selectedRbData = radioButtonsData.find { it.isSelected } ?: radioButtonsData[0]
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = { }
        ) {
            // sheetContent MUST have at least one composable or an exception is thrown
            LazyColumn {
                items(selectedRbData.itemCount) {
                    val listItemText = "Item $it"
                    ListItem(
                        modifier = Modifier.clickable {
                            text = listItemText
                            scope.launch { bottomSheetState.hide() }
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
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = Screen.MODAL_BOTTOM_SHEET.title,
                onBack = onBack,
                actions = {
                    IconButton(onClick = { applyPadding = !applyPadding }) { Icon(imageVector = Icons.Default.Padding, contentDescription = null) }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .then(if (applyPadding) Modifier.padding(paddingValues) else Modifier)
        ) {
            items(radioButtonsData) { rbData ->
                RadioButtonAndText(
                    text = rbData.text,
                    selected = rbData.isSelected,
                    onClick = {
                        scope.launch {
                            bottomSheetState.expand()
                            radioButtonsData = radioButtonsData.map { it.copy(isSelected = it.text == rbData.text) }
                        }
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