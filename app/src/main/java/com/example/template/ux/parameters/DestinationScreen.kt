package com.example.template.ux.parameters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.domain.Parameter
import com.example.template.ui.composable.AppTopAppBar
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation3.navigator.Navigation3Navigator
import org.lds.mobile.ui.ext.requireActivity

@Composable
fun DestinationScreen(navigator: Navigation3Navigator, viewModel: DestinationViewModel) {
    val context = LocalContext.current
    DestinationContent(viewModel.uiState) { if (viewModel.uiState.onCloseBack) context.requireActivity().finishAffinity() else navigator.pop() }
}

@Composable
fun DestinationContent(uiState: DestinationUiState, onBack: () -> Unit) {
    val required by uiState.reqParam1Flow.collectAsStateWithLifecycle()
    val enumParameter by uiState.reqParam2Flow.collectAsStateWithLifecycle()
    val optional by uiState.optParam1Flow.collectAsStateWithLifecycle()
    val optionalEnumParameter by uiState.optParam2Flow.collectAsStateWithLifecycle()

    Scaffold(topBar = { AppTopAppBar(title = "Destination Screen", onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row {
                Text(text = "Required 1: ")
                Text(text = required.value)
            }
            Row {
                Text(text = "Required 2: ")
                Text(text = enumParameter.toString())
            }
            Row {
                Text(text = "Optional 1: ")
                optional?.value?.let { Text(text = it) }
            }
            Row {
                Text(text = "Optional 2: ")
                Text(text = optionalEnumParameter?.toString().orEmpty())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DestinationContentPreview() {
    val mockUiState = DestinationUiState(
        reqParam1Flow = MutableStateFlow(Parameter("Sample Value")),
        reqParam2Flow = MutableStateFlow(EnumParameter.TWO),
        optParam1Flow = MutableStateFlow(Parameter("Optional Value")),
        optParam2Flow = MutableStateFlow(EnumParameter.THREE)
    )
    DestinationContent(uiState = mockUiState, onBack = {})
}
