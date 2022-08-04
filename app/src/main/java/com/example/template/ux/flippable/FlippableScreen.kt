package com.example.template.ux.flippable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.rememberFlipController

@Composable
fun FlippableScreen(navController: NavController) {
    FlippableContent(navController::popBackStack)
}

@Composable
fun FlippableContent(onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.FLIPPABLE.title, onBack = onBack) }) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val modifier = if (maxWidth < maxHeight) {
                Modifier
                    .fillMaxWidth(.8F)
                    .aspectRatio(1F)
            } else {
                Modifier
                    .fillMaxHeight(.8F)
                    .aspectRatio(1F)
            }

            Flippable(
                frontSide = {
                    Card(modifier = modifier, backgroundColor = Color.Yellow) {
                        Text(text = "Side 1")
                    }
                },

                backSide = {
                    Card(modifier = modifier, backgroundColor = Color.Cyan) {
                        Text(text = "Side 2")
                    }
                },

                flipController = rememberFlipController(),

                // Other optional parameters
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@PreviewDefault
@Composable
private fun FlippableContentPreview() {
    AppTheme { FlippableContent() }
}