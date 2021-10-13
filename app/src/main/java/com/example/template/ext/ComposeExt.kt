package com.example.template.ext

import androidx.compose.ui.unit.IntSize

fun IntSize.findBestFit(parentWidth: Float, parentHeight: Float, padding: Float = 0F): IntSize {
    val parentRatio = parentWidth / parentHeight
    val ratio = width / height.toFloat()

    return if (parentRatio < 1) {
        // Fit width
        IntSize((parentWidth - (padding * 2)).toInt(), ((parentWidth / ratio) - (padding * 2)).toInt())
    } else {
        // Fit height
        IntSize(((parentHeight * ratio) - (padding * 2)).toInt(), (parentHeight - (padding * 2)).toInt())
    }
}