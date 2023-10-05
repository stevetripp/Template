package com.example.template.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme


@Composable
fun EmptyStateDialog(uiState: EmptyStateDialogUiState) {
    Dialog(onDismissRequest = { uiState.onDismissRequest?.invoke() }) {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    modifier = Modifier.size(148.dp),
                    imageVector = uiState.imageVector,
                    contentDescription = uiState.text,
                    tint = AppTheme.colors.outline,
                )

                Text(modifier = Modifier.padding(bottom = 16.dp), text = uiState.text, style = AppTheme.typography.headlineSmall, textAlign = TextAlign.Center)

                uiState.subtext?.let {
                    Text(
                        modifier = Modifier.padding(bottom = 16.dp),
                        text = it, textAlign = TextAlign.Center, style = AppTheme.typography.bodySmall
                    )
                }

                uiState.buttonText?.let {
                    OutlinedButton(onClick = uiState.onButtonClicked) { Text(uiState.buttonText) }
                }

                uiState.actionText?.let {
                    TextButton(modifier = Modifier.padding(bottom = 16.dp), onClick = uiState.onActionTextClicked) {
                        Text(text = it, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

data class EmptyStateDialogUiState(
    val imageVector: ImageVector,
    val text: String,
    val modifier: Modifier = Modifier,
    val subtext: String? = null,
    val buttonText: String? = null,
    val onButtonClicked: () -> Unit = { },
    val actionText: String? = null,
    val onActionTextClicked: () -> Unit = {},
    val showIndeterminateProgress: Boolean = false,
    override val onConfirm: ((Unit) -> Unit)? = null,
    override val onDismiss: (() -> Unit)? = null,
    override val onDismissRequest: (() -> Unit)? = null,
) : DialogUiState<Unit>

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme {
        EmptyStateDialog(
            EmptyStateDialogUiState(
                imageVector = Icons.Default.Handshake,
                text = "Dialog Test",
                onConfirm = {},
                onDismiss = {},
                onDismissRequest = {},
            )
        )
    }
}