package com.example.template.ui.dialog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme


@Composable
fun ExampleDialog(uiState: ExampleDialogUiState) {
    Dialog(onDismissRequest = { uiState.onDismissRequest?.invoke() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = uiState.title,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}

data class ExampleDialogUiState(
    val title: String,
    override val onConfirm: ((Unit) -> Unit)?,
    override val onDismiss: (() -> Unit)?,
    override val onDismissRequest: (() -> Unit)?,
) : DialogUiState<Unit>

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { ExampleDialog(ExampleDialogUiState(title = "Title", onConfirm = {}, onDismiss = {}, onDismissRequest = {})) }
}
