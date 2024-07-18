package com.example.template.ux.parameters

import androidx.compose.foundation.layout.Column
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
import com.example.template.util.SmtLogger
import com.example.template.ux.main.Screen
import org.lds.mobile.ui.compose.navigation.HandleNavigation

@Composable
fun ParametersScreen(navController: NavController, viewModel: ParametersViewModel = hiltViewModel()) {
    ParametersContent(viewModel.uiState, navController::navigateUp)
    HandleNavigation(viewModel, navController)
}

@Composable
fun ParametersContent(uiState: ParametersUiState, onBack: () -> Unit) {
    val requiredValue by uiState.requiredValueFlow.collectAsStateWithLifecycle()
    val enumParameter by uiState.enumParameterFlow.collectAsStateWithLifecycle()
    val optionalValue by uiState.optionalValueFlow.collectAsStateWithLifecycle()
    val options = mutableListOf<DropdownOption>()
    EnumParameter.entries.forEach { options.add(DropdownOption(it.value, it.name)) }

    Scaffold(topBar = { AppTopAppBar(title = Screen.PARAMETERS.title, onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextField(value = requiredValue, onValueChange = uiState.onRequiredValueChanged)
            DropdownList(
                value = enumParameter.value,
                label = "Options",
                options = options,
                onValueChanged = {
                    val param = EnumParameter.valueOf(it.selectedValue)
                    SmtLogger.i("""${it.selectedValue}, $param""")
                    uiState.onEnumParameterChanged(param)
                }
            )
            TextField(value = optionalValue.orEmpty(), onValueChange = uiState.onOptionalValueChanged)
            Button(onClick = uiState.onButtonClick) { Text(text = "Tap Me") }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { ParametersContent(ParametersUiState(), onBack = {}) }
}