package com.example.template.ux.bottomSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme

@Composable
fun BottomSheetContent(modifier: Modifier = Modifier, onHide: () -> Unit) {
    Surface {
        Column(
            modifier
                .fillMaxWidth()
                .fillMaxHeight(.8f)
                .padding(bottom = 16.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(title = { Text(text = "Sheet content") })
            Button(onClick = onHide) { Text("Click to collapse sheet") }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { BottomSheetContent(onHide = {}) }
}