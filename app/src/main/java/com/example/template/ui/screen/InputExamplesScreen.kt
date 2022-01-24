package com.example.template.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.template.AppBar
import com.example.template.Nav
import com.example.template.ui.composable.DropDownList

@Composable
fun InputExamplesScreen(nav: Nav, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    val options = mutableListOf<String>()
    (1..10).forEach { options.add("Option $it") }
    var selectedOption by remember { mutableStateOf("") }
    Scaffold(topBar = { AppBar(nav, onBack) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            DropDownList(
                value = selectedOption,
                label = "Options",
                options = options,
                onValueChanged = { selectedOption = it }
            )
        }
    }
}