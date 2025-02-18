package com.example.template.ux.fab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material.icons.filled.DonutSmall
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MultipleStop
import androidx.compose.ui.graphics.vector.ImageVector

enum class FabType(val imageVector: ImageVector) {
    DEFAULT(Icons.Default.Home),
    EXTENDED(Icons.Default.Extension),
    MULTIPLE(Icons.Default.MultipleStop),
    LARGE(Icons.Default.DonutLarge),
    SMALL(Icons.Default.DonutSmall),
}