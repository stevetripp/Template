package com.example.template.ux.popwithresult

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import org.lds.mobile.navigation3.navigator.Navigation3Navigator
import org.lds.mobile.ui.compose.navigation.HandleNavigation3

@Composable
fun PopWithResultChildScreen(navigator: Navigation3Navigator, viewModel: PopWithResultChildViewModel) {
    PopWithResultChildContent(viewModel.uiState)
    HandleNavigation3(viewModelNavigation = viewModel, navigator = navigator)
}

@Composable
private fun PopWithResultChildContent(uiState: PopWithResultChildUiState) {
    val value by uiState.valueFlow.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { AppTopAppBar(title = "Pop With Result Child", onBack = uiState.onPopBackStack) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            TextField(value = value, onValueChange = uiState.onValueChanged)
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { PopWithResultChildContent(PopWithResultChildUiState()) }
}