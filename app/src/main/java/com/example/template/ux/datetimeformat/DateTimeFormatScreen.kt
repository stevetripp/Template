package com.example.template.ux.datetimeformat

import android.content.res.Configuration
import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        )
        {
            ReadOnlyTextField(value = now.toString(), label = "UTC")
            ReadOnlyTextField(value = Locale.getDefault().displayName, label = "Locale")
            ReadOnlyTextField(value = now.toLocalDateTime(TimeZone.currentSystemDefault()).toString(), label = "Local DateTime")
            ReadOnlyTextField(value = DateUtils.formatDateTime(context, now.toEpochMilliseconds(), SHOW_NUMERIC_DATE_YEAR_TIME), label = "SHOW_NUMERIC_DATE_YEAR_TIME")
            ReadOnlyTextField(value = DateUtils.formatDateTime(context, now.toEpochMilliseconds(), SHOW_DATE_YEAR_TIME), label = "SHOW_DATE_YEAR_TIME")
        }
    }
}

private const val SHOW_DATE_YEAR_TIME = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_TIME
private const val SHOW_NUMERIC_DATE_YEAR_TIME = SHOW_DATE_YEAR_TIME or DateUtils.FORMAT_NUMERIC_DATE

@Composable
fun ReadOnlyTextField(value: String, label: String) {
    TextField(modifier = Modifier.fillMaxWidth(), value = value, onValueChange = {}, label = { Text(text = label) }, readOnly = true)
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