package com.example.template.ux.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.PreviewPhoneOrientations
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import org.lds.mobile.ui.compose.material3.setting.Setting

@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel = hiltViewModel()) {
    SettingsContent(viewModel.uiState, navController::popBackStack)
}

@Composable
private fun SettingsContent(uiState: SettingsUiState, onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = "Settings", onBack = onBack) }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Setting.Switch(
                text = "Enforce Navigation Bar Contrast",
                currentCheckedValueFlow = uiState.enforceNavigationBarContrastFlow,
                secondaryText = "Default is set to true.",
                onClickBody = uiState.onEnforceNavigationBarContrastClicked,
            )
        }
    }
}

@PreviewPhoneOrientations
@Composable
private fun Preview() {
    AppTheme { SettingsContent(SettingsUiState()) }
}