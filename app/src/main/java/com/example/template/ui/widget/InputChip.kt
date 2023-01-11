package com.example.template.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.template.R
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme

@Composable
fun InputChip(
    label: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppTheme.colors.primary,
    leadingIcon: @Composable (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(CornerSize(24.dp)))
            .background(backgroundColor)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingIcon?.invoke()
        Text(modifier = Modifier.padding(horizontal = 8.dp), text = label)
        onRemove?.let {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.onBackground)
                    .clickable { onRemove() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = AppTheme.colors.background,
                )
            }
        }
    }
}

@PreviewDefault
@Composable
private fun InputChipPreview() {
    AppTheme {
        Column {
            InputChip(
                label = "The Label",
                leadingIcon = { Icon(imageVector = Icons.Default.RemoveCircle, contentDescription = null) },
                onRemove = {}
            )
            InputChip(
                label = "The Label",
                leadingIcon = {
                    AsyncImage(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.Magenta),
                        model = "",
                        placeholder = painterResource(id = R.drawable.coloring_book),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                },
                onRemove = {}
            )
        }
    }
}