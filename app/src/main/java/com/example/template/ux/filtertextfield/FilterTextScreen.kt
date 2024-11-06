package com.example.template.ux.filtertextfield

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.PreviewPhoneOrientations
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.composable.FilterTextField
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import com.example.template.ux.search.SearchViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.roundToInt

@Composable
fun FilterTextScreen(navController: NavController, viewModel: FilterTextViewModel = hiltViewModel()) {
    FilterTextContent(viewModel.uiState, navController::popBackStack)
}

@Composable
private fun FilterTextContent(uiState: FilterTextUiState, onBack: () -> Unit = {}) {
    val names by uiState.namesFlow.collectAsStateWithLifecycle()
    val query by uiState.queryFlow.collectAsStateWithLifecycle()

    val density = LocalDensity.current

    var searchBarSize by remember { mutableStateOf(IntSize(0, 0)) }
    val searchBarHeightDp by remember(searchBarSize) { mutableStateOf(with(density) { searchBarSize.height.toDp() }) }
    var searchBarHeightOffsetPx by remember { mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember(searchBarSize) {
        object : NestedScrollConnection {
            val filterTextFieldHeightPx = searchBarSize.height.toFloat()

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (query.isEmpty()) {
                    val delta = available.y
                    val newOffset = searchBarHeightOffsetPx + delta
                    searchBarHeightOffsetPx = newOffset.coerceIn(-filterTextFieldHeightPx, 0f)
                }
                return Offset.Zero
            }
        }
    }

    Scaffold(
        topBar = { AppTopAppBar(title = Screen.FILTER_TEXT.title, onBack = onBack) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(nestedScrollConnection)
        ) {
            FilterTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .zIndex(1f)
                    .offset { IntOffset(0, searchBarHeightOffsetPx.roundToInt()) }
                    .onSizeChanged { searchBarSize = it },
                query = query,
                placeholder = "Placeholder",
                onQueryChange = uiState.onQueryChange
            )
            LazyColumn(contentPadding = PaddingValues(top = searchBarHeightDp)) {
                items(names) { name ->
                    ListItem(headlineContent = { Text(text = name) })
                }
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