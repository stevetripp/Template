package com.example.template.ux.parameters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.composable.DropdownList
import com.example.template.ui.composable.DropdownOption
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import org.lds.mobile.ui.compose.navigation.HandleNavigation

@Composable
fun ParametersScreen(navController: NavController, viewModel: ParametersViewModel = hiltViewModel()) {
    ParametersContent(viewModel.uiState, navController::navigateUp)
    HandleNavigation(viewModel, navController)
}

@Composable
fun ParametersContent(uiState: ParametersUiState, onBack: () -> Unit) {
    val requiredValue by uiState.reqParam1Flow.collectAsStateWithLifecycle()
    val enumParameter by uiState.reqParam2Flow.collectAsStateWithLifecycle()
    val optionalValue by uiState.optParam1Flow.collectAsStateWithLifecycle()
    val optionalEnumParameter by uiState.optParam2Flow.collectAsStateWithLifecycle()
    val options = mutableListOf<DropdownOption>()
    EnumParameter.entries.forEach { options.add(DropdownOption(it.name, it.name)) }

    Scaffold(topBar = { AppTopAppBar(title = Screen.PARAMETERS.title, onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            TextField(modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(), value = requiredValue, onValueChange = uiState.onReqParam1Changed)
            DropdownList(
                value = enumParameter.name,
                label = "Required Enum",
                options = options,
                onValueChanged = {
                    val param = EnumParameter.valueOf(it.selectedValue)
                    uiState.onReqParam2Changed(param)
                }
            )
            TextField(modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(), value = optionalValue.orEmpty(), onValueChange = uiState.onOptParam1Changed)
            DropdownList(
                value = optionalEnumParameter?.name.orEmpty(),
                label = "Optional Enum",
                options = options,
                onValueChanged = {
                    val param = EnumParameter.valueOf(it.selectedValue)
                    uiState.onOptParam2Changed(param)
                }
            )
            Button(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),onClick = uiState.onButtonClick) { Text(text = "Tap Me") }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { ParametersContent(ParametersUiState(), onBack = {}) }
}