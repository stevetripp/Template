package com.example.template.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.template.AppBar
import com.example.template.Nav

@Composable
fun Home(nav: Nav, onNavigate: (Nav) -> Unit) {
    Scaffold(topBar = { AppBar(nav) }) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
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