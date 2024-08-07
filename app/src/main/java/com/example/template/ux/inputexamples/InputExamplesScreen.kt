package com.example.template.ux.inputexamples

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.composable.DropdownList
import com.example.template.ui.composable.DropdownOption
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen

@Composable
fun InputExamplesScreen(navController: NavController) {
    InputExamplesContent(navController::popBackStack)
}

@Composable
fun InputExamplesContent(onBack: () -> Unit = {}) {
    val options = mutableListOf<DropdownOption>()
    for (index: Int in 1..10) {
        options.add(DropdownOption("Option $index*", "Option $index"))
    }
    var selectedOption by remember { mutableStateOf(DropdownOption("")) }
    Scaffold(topBar = { AppTopAppBar(title = Screen.INPUT_EXAMPLES.title, onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            DropdownList(
                value = selectedOption.selectedValue,
                label = "Options",
                options = options,
                onValueChanged = { selectedOption = it }
            )
        }
    }
}

@PreviewDefault
@Composable
private fun InputExamplesContentPreview() {
    AppTheme { InputExamplesContent() }
}