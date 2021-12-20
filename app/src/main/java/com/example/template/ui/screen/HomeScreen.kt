package com.example.template.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.template.AppBar
import com.example.template.Nav
import com.example.template.ui.theme.AppTheme

@Composable
fun HomeScreen(nav: Nav, onNavigate: (Nav) -> Unit) {
    val scrollState = rememberScrollState()
    Scaffold(topBar = { AppBar(nav) }) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Nav.values().filterNot { it == Nav.HOME }.forEach { destination ->
                TextButton(
                    onClick = { onNavigate(destination) },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = ButtonDefaults.textButtonColors(backgroundColor = MaterialTheme.colors.surface)
                ) {
                    Text(destination.title)
                }
            }
        }
    }
}

@Preview(group = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true)
@Preview(group = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true)
@Composable
private fun HomeScreenPreview() {
    AppTheme { HomeScreen(nav = Nav.HOME, onNavigate = {}) }
}