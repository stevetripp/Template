package com.example.template.ux.edgetoedge

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.PreviewPhoneOrientations
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme

@Composable
fun EdgeToEdgeScreen(navController: NavController, viewModel: EdgeToEdgeViewModel = hiltViewModel()) {
    EdgeToEdgeContent(viewModel.uiState, navController::popBackStack)
}

@Composable
private fun EdgeToEdgeContent(uiState: EdgeToEdgeUiState, onBack: () -> Unit = {}) {
    var showTopBar by remember { mutableStateOf(true) }
    var showBottomAppBar by remember { mutableStateOf(true) }
    var showNavigationRail by remember { mutableStateOf(true) }
    var applyScaffoldPadding by remember { mutableStateOf(true) }
    val enforceNavigationBarContrast by uiState.enforceNavigationBarContrastFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { if (showTopBar) AppTopAppBar(title = "The Title", onBack = onBack) },
        bottomBar = {
            if (!AppTheme.isLandscape && showBottomAppBar) {
                BottomAppBar(
                    modifier = Modifier
                        .padding(1.dp)
                        .border(width = 2.dp, color = MaterialTheme.colorScheme.onBackground)
                ) { }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(if (applyScaffoldPadding) Modifier.padding(paddingValues) else Modifier)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
                    .border(width = 2.dp, color = MaterialTheme.colorScheme.onPrimaryContainer)
            ) {
                if (AppTheme.isLandscape && showNavigationRail) AppSideAppBar(
                    modifier = Modifier
                        .padding(1.dp)
                        .border(width = 2.dp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                ) { }
                Column(
                    modifier = Modifier
                        .weight(.9f)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    EdgeToEdgeListItem("Hide/Show TopBar", showTopBar, onToggle = { showTopBar = it })
                    EdgeToEdgeListItem("Hide/Show BottomAppBar", showBottomAppBar, onToggle = { showBottomAppBar = it })
                    EdgeToEdgeListItem("Hide/Show NavigationRails", showNavigationRail, onToggle = { showNavigationRail = it })
                    EdgeToEdgeListItem("Apply Scaffold Padding", applyScaffoldPadding, onToggle = { applyScaffoldPadding = it })
                    EdgeToEdgeListItem("Enforce Navigation Bar Contrast", enforceNavigationBarContrast, onToggle = { uiState.onToggleEnforceNavigationBarContrast(it) })
                }
                if (AppTheme.isLandscape && showNavigationRail) AppSideAppBar(
                    modifier = Modifier
                        .padding(1.dp)
                        .border(width = 2.dp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                ) { }
            }
        }
    }
}

@PreviewPhoneOrientations
@Composable
private fun Preview() {
    AppTheme { EdgeToEdgeContent(EdgeToEdgeUiState()) }
}