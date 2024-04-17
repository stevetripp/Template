package com.example.template.ux.datetimeformat

import android.content.res.Configuration
import android.text.format.DateUtils
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import kotlinx.datetime.Clock
import java.util.Locale

@Composable
fun DateTimeFormatScreen(navController: NavController) {
    DateTimeFormatContent(navController::popBackStack)
}

@Composable
private fun DateTimeFormatContent(onBack: () -> Unit = {}) {
    val context = LocalContext.current
    val now = Clock.System.now()
    Scaffold(topBar = { AppTopAppBar(title = Screen.DATE_TIME_FORMAT.title, onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        )
        {
            Text(text = "Locale: ${Locale.getDefault().displayName}")
            Text(
                text = DateUtils.formatDateTime(
                    context,
                    now.toEpochMilliseconds(),
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_TIME
                )
            )
            Text(text = DateUtils.formatDateTime(context, now.toEpochMilliseconds(), DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_TIME))
        }
    }
}

@Preview(group = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true, locale = "en")
//@Preview(group = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true, locale = "fr")
//@Preview(group = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true, locale = "de")
//@Preview(group = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true, locale = "zh")
@Preview(group = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true, locale = "ja")
//@Preview(group = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true, locale = "en-AU")
@Composable
private fun Preview() {
    AppTheme { DateTimeFormatContent() }
}