package com.example.template.ux.filtertextfield

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.PreviewPhoneOrientations
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.composable.CoordinatedLazyColumn
import com.example.template.ui.composable.FilterTextField
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import com.example.template.ux.search.SearchViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun FilterTextScreen(navController: NavController, viewModel: FilterTextViewModel = hiltViewModel()) {
    FilterTextContent(viewModel.uiState, navController::popBackStack)
}

@Composable
private fun FilterTextContent(uiState: FilterTextUiState, onBack: () -> Unit = {}) {
    val names by uiState.namesFlow.collectAsStateWithLifecycle()
    val query by uiState.queryFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopAppBar(title = Screen.FILTER_TEXT.title, onBack = onBack) }
    ) { paddingValues ->
        CoordinatedLazyColumn(modifier = Modifier.padding(paddingValues),
            pinCoordinatedContent = query.isBlank(),
            coordinatedContent = { coordinatedModifier ->
                FilterTextField(
                    modifier = coordinatedModifier.padding(horizontal = 16.dp),
                    query = query,
                    placeholder = "Placeholder",
                    onQueryChange = uiState.onQueryChange
                )
            }) {
            items(names) { name ->
                ListItem(headlineContent = { Text(text = name) })
            }
        }
    }
}

@PreviewPhoneOrientations
@Composable
private fun Preview() {
    val listFlow = MutableStateFlow(SearchViewModel.randomNames)
    AppTheme { FilterTextContent(FilterTextUiState(namesFlow = listFlow)) }
}