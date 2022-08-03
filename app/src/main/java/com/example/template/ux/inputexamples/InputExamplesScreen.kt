package com.example.template.ux.inputexamples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ux.main.Screen
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.composable.DropDownList
import com.example.template.ui.theme.AppTheme

@Composable
fun InputExamplesScreen(navController: NavController) {
    InputExamplesContent(navController::popBackStack)
}

@Composable
fun InputExamplesContent(onBack: () -> Unit = {}) {
    val options = mutableListOf<String>()
    (1..10).forEach { options.add("Option $it") }
    var selectedOption by remember { mutableStateOf("") }
    Scaffold(topBar = { AppTopAppBar(title = Screen.INPUT_EXAMPLES.title, onBack = onBack) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            DropDownList(
                value = selectedOption,
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