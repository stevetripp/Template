package com.example.template.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme

@Composable
fun UnderlinedRedactedText(
    text: String,
    ranges: List<AnnotatedString.Range<Boolean>>,
    modifier: Modifier = Modifier,
    underlineColor: Color = MaterialTheme.colorScheme.onBackground,
    style: TextStyle = LocalTextStyle.current,
) {
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val textModifier = Modifier
        .fillMaxWidth()

    Box(modifier = modifier.fillMaxWidth()) {
        RedactedText(
            text = text,
            onTextLayout = { textLayoutResult = it },
            modifier = textModifier,
            style = style,
            ranges = ranges,
        )
        textLayoutResult?.let { tlr ->
            val rects = ranges.filter { it.item }.map {
                val startRect = tlr.getBoundingBox(it.start)
                val endRect = tlr.getBoundingBox(it.end)
                Rect(left = startRect.left, top = startRect.top, right = endRect.right, bottom = endRect.bottom - 4f)
            }

            Text(
                text = "",
                style = style,
                modifier = textModifier
                    .drawBehind {
                        rects.forEach {
                            drawLine(
                                color = underlineColor,
                                start = Offset(it.left, it.bottom),
                                end = Offset(it.right, it.bottom),
                                strokeWidth = 4f
                            )
                        }
                    }
            )
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    val text = "Aenean tellus metus, bibendum sed, posuere ac, mattis non, nunc."
    val ranges = listOf(
        AnnotatedString.Range(false, 7, 12, ""),
        AnnotatedString.Range(false, 35, 41, ""),
        AnnotatedString.Range(false, 47, 52, ""),
        AnnotatedString.Range(true, 59, 62, ""),
    )
    AppTheme {
        Surface {
            UnderlinedRedactedText(
                text = text,
                ranges = ranges,
                underlineColor = Color.Blue
            )
        }
    }
}