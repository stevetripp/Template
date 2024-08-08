package com.example.template.ux.flippable

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.rememberFlipController
import org.lds.mobile.ui.ext.popBackStackOrFinishActivity

@Composable
fun FlippableScreen(navController: NavController) {
    val context = LocalContext.current
    FlippableContent {
        // This is needed for deep linking to close the app when tapping back
        navController.popBackStackOrFinishActivity(context)
    }
}

@Composable
fun FlippableContent(onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.FLIPPABLE.title, onBack = onBack) }) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            var width = maxWidth.times(.8F)
            var height = maxHeight.times(.8F)

            if (maxWidth < maxHeight) {
                height = width
            } else {
                width = height
            }

            val modifier = if (maxWidth < maxHeight) {
                Modifier
                    .width(width)
                    .defaultMinSize(minHeight = height)
            } else {
                Modifier
                    .defaultMinSize(minHeight = height)
                    .requiredWidthIn(width, maxWidth.times(.8F))
            }

            val scrollState = rememberScrollState()

            Flippable(
                frontSide = {
                    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color.Yellow)) {
                        Text(
                            modifier = Modifier.verticalScroll(scrollState),
                            text =
                            """Praesent sapien massa, convallis a pellentesque nec, egestas non nisi. Proin eget tortor risus. Cras ultricies ligula sed magna dictum porta. Sed porttitor lectus 
                                |nibh. Praesent sapien massa, convallis a pellentesque nec, egestas non nisi. 
                                |
                                |Mauris blandit aliquet elit, eget tincidunt nibh pulvinar a. Proin eget tortor risus. Praesent sapien massa, convallis a pellentesque nec, egestas non nisi. 
                                |Quisque velit nisi, pretium ut lacinia in, elementum id enim. Vivamus magna justo, lacinia eget consectetur sed, convallis at tellus. 
                                |
                                |Vestibulum ac diam sit amet quam vehicula elementum sed sit amet dui. Curabitur arcu erat, accumsan id imperdiet et, porttitor at sem. Lorem ipsum dolor sit
                                |amet, consectetur adipiscing elit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec velit neque, auctor sit amet 
                                |aliquam vel, ullamcorper sit amet ligula. Cras ultricies ligula sed magna dictum porta.
                                |
                                |Mauris blandit aliquet elit, eget tincidunt nibh pulvinar a. Quisque velit nisi, pretium ut lacinia in, elementum id enim. Lorem ipsum dolor sit amet, consectetur
                                |adipiscing elit. Pellentesque in ipsum id orci porta dapibus. Vestibulum ac diam sit amet quam vehicula elementum sed sit amet dui.
                                |
                                |Curabitur aliquet quam id dui posuere blandit. Nulla quis lorem ut libero malesuada feugiat. Proin eget tortor risus. Nulla quis lorem ut libero malesuada 
                                |feugiat. Curabitur aliquet quam id dui posuere blandit.""".trimMargin()
                        )
                    }
                },

                backSide = {
                    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color.Yellow)) {
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