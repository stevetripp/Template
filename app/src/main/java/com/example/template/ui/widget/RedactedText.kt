package com.example.template.ui.widget

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme


@Composable
fun RedactedText(
    text: String,
    ranges: List<AnnotatedString.Range<Boolean>>,
    modifier: Modifier = Modifier,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
) {
    val annotatedString = buildAnnotatedString {

        text.forEachIndexed { index, c ->
            val range = ranges.find { index in it.start..it.end }
            if (range?.item == true) {
                withStyle(
                    style = SpanStyle(color = Color.Transparent)
                ) {
                    append(c)
                }
            } else {
                append(c)
            }
        }
    }

    Text(
        text = annotatedString,
        onTextLayout = onTextLayout,
        modifier = modifier,
        style = style,
    )
}

@PreviewDefault
@Composable
private fun Preview() {
    val text = "Aenean tellus metus, bibendum sed, posuere ac, mattis non, nunc."
    val ranges = listOf(
        AnnotatedString.Range(false, 7, 12, ""),
        AnnotatedString.Range(true, 30, 32, ""),
        AnnotatedString.Range(false, 47, 52, ""),
        AnnotatedString.Range(true, 59, 62, ""),
    )

    AppTheme { Surface { RedactedText(text = text, ranges = ranges) } }
}
