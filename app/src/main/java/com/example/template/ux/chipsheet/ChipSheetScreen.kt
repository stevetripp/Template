package com.example.template.ux.chipsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ui.widget.ChipDropDown
import com.example.template.ui.widget.ChipItem
import com.example.template.ui.widget.ChipItemsPreviewParameterProvider
import com.example.template.ui.widget.ChipModalBottomSheet
import com.example.template.ux.main.Screen
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ChipSheetScreen(navController: NavController, viewModel: ChipSheetViewModel = hiltViewModel()) {
    ChipSheetContent(viewModel.uiState, navController::popBackStack)
}

@Composable
fun ChipSheetContent(uiState: ChipSheetUiState, onBack: () -> Unit = {}) {
    val numberItems by uiState.numberItemsFlow.collectAsStateWithLifecycle()
    val animalItems by uiState.animalItemsFlow.collectAsStateWithLifecycle()
    val colorItems by uiState.colorItemsFlow.collectAsStateWithLifecycle()

    Scaffold(topBar = { AppTopAppBar(title = Screen.CHIP_SHEET.title, onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ChipModalBottomSheet(chipItems = numberItems, onItemSelected = uiState.onNumberItemSelected)
                ChipModalBottomSheet(chipItems = animalItems, onItemSelected = uiState.onAnimalItemSelected)
                ChipDropDown(chipItems = colorItems, onSelected = uiState.onColorSelected)
            }

            Button(onClick = uiState.onExecute) { Text(text = "Execute") }
        }
    }
}

@PreviewDefault
@Composable
private fun ChipBottomSheetContentPreview(
    @PreviewParameter(ChipItemsPreviewParameterProvider::class) items: List<ChipItem>
) {
    AppTheme {
        ChipSheetContent(
            ChipSheetUiState(
                numberItemsFlow = MutableStateFlow(items),
                animalItemsFlow = MutableStateFlow(items),
                colorItemsFlow = MutableStateFlow(items),
            )
        )
    }
}