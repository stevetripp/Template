package com.example.template.ux.buttongroups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Anchor
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen

@Composable
fun ButtonGroupsScreen(navController: NavController) {
    ButtonGroupsContent(navController::popBackStack)
}

@Composable
fun ButtonGroupsContent(onBack: () -> Unit = {}) {
    Scaffold(
        topBar = { AppTopAppBar(title = Screen.BUTTON_GROUPS.title, onBack = onBack) },
    ) {
        var selectedButton by remember { mutableStateOf(SelectableButton.entries.first()) }

        Row(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterHorizontally)
        ) {
            SelectableButton.entries.forEach { selectableButton ->
                FilterChip(
                    selected = selectableButton == selectedButton,
                    onClick = { selectedButton = selectableButton },
                    label = {
                        Icon(imageVector = selectableButton.imageVector, contentDescription = null)
                    },
                )
            }
        }
    }
}

private enum class SelectableButton(val imageVector: ImageVector) {
    HOME(Icons.Default.Home),
    LIGHTBULB(Icons.Default.Lightbulb),
    ANCHOR(Icons.Default.Anchor),
    CARD_TRAVEL(Icons.Default.CardTravel);
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { ButtonGroupsContent() }
}