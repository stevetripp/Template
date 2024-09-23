package com.example.template.ux.typography

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen

@Composable
fun TypographyScreen(navController: NavController) {
    TypographyContent(onBack = navController::popBackStack)
}

enum class TypographyStyle(val textStyle: @Composable () -> TextStyle, val styleName: String) {
    DISPLAY_LARGE({ MaterialTheme.typography.displayLarge }, "Display Large"),
    DISPLAY_MEDIUM({ MaterialTheme.typography.displayMedium }, "Display Medium"),
    DISPLAY_SMALL({ MaterialTheme.typography.displaySmall }, "Display Small"),
    HEADLINE_LARGE({ MaterialTheme.typography.headlineLarge }, "Headline Large"),
    HEADLINE_MEDIUM({ MaterialTheme.typography.headlineMedium }, "Headline Medium"),
    HEADLINE_SMALL({ MaterialTheme.typography.headlineSmall }, "Headline Small"),
    TITLE_LARGE({ MaterialTheme.typography.titleLarge }, "Title Large"),
    TITLE_MEDIUM({ MaterialTheme.typography.titleMedium }, "Title Medium"),
    TITLE_SMALL({ MaterialTheme.typography.titleSmall }, "Title Small"),
    BODY_LARGE({ MaterialTheme.typography.bodyLarge }, "Body Large"),
    BODY_MEDIUM({ MaterialTheme.typography.bodyMedium }, "Body Medium"),
    BODY_SMALL({ MaterialTheme.typography.bodySmall }, "Body Small"),
    LABEL_LARGE({ MaterialTheme.typography.labelLarge }, "Label Large"),
    LABEL_MEDIUM({ MaterialTheme.typography.labelMedium }, "Label Medium"),
    LABEL_SMALL({ MaterialTheme.typography.labelSmall }, "Label Small"),
}

@Composable
private fun TypographyContent(onBack: () -> Unit={}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.TYPOGRAPHY.title, onBack = onBack) }) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            items(TypographyStyle.entries) { typographyStyle ->
                val style = typographyStyle.textStyle()
                val text = "${typographyStyle.styleName} (${style.fontSize})"
                Text(text = text, style = typographyStyle.textStyle())
            }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { TypographyContent() }
}