package com.example.template.ux.fab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.model.data.SelectedObject
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun FabScreen(navController: NavController, viewModel: FabViewModel = hiltViewModel()) {
    FabContent(viewModel.uiState, onBack = navController::popBackStack)
}

@Composable
fun FabContent(uiState: FabScreenUiState, onBack: () -> Unit = {}) {
    val fabTypes by uiState.fabTypesFlow.collectAsStateWithLifecycle()
    val selectedFabType = fabTypes.find { it.isSelected }?.obj ?: FabType.DEFAULT

    fun floatingActionButton(): @Composable () -> Unit = {
        when (selectedFabType) {
            FabType.SMALL -> SmallFloatingActionButton(onClick = {}) { Icon(selectedFabType.imageVector, contentDescription = null) }
            FabType.LARGE -> LargeFloatingActionButton(onClick = {}) { Icon(selectedFabType.imageVector, contentDescription = null) }
            FabType.EXTENDED -> ExtendedFloatingActionButton(onClick = {}) {
                Icon(selectedFabType.imageVector, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = selectedFabType.name)
            }

            FabType.DEFAULT -> FloatingActionButton(onClick = {}) { Icon(selectedFabType.imageVector, contentDescription = null) }
            FabType.MULTIPLE -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SmallFloatingActionButton(onClick = {}) { Icon(FabType.SMALL.imageVector, contentDescription = null) }
                    FloatingActionButton(onClick = {}) { Icon(FabType.DEFAULT.imageVector, contentDescription = null) }
                }
            }
        }
    }

    Scaffold(
        topBar = { AppTopAppBar(title = Screen.FAB.title, onBack = onBack) },
        floatingActionButton = floatingActionButton()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)

        ) {
            fabTypes.forEach { fabType ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            fabType.isSelected,
                            onValueChange = { uiState.onFabTypeChanged(fabType.obj) },
                            role = Role.RadioButton
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = fabType.isSelected,
                        onClick = { uiState.onFabTypeChanged(fabType.obj) },
                    )
                    Text(text = fabType.obj.name)
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    val selectedFabTypes = FabType.entries.mapIndexed { index, obj -> SelectedObject(obj, index == 0) }
    val uiState = FabScreenUiState(fabTypesFlow = MutableStateFlow(selectedFabTypes))
    AppTheme { FabContent(uiState) }
}