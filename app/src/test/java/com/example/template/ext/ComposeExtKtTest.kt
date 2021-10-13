package com.example.template.ext

import androidx.compose.ui.unit.IntSize
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ComposeExtKtTest {
    @Test
    fun findBestFit_tallParent() {
        val sut = IntSize(100, 200)
        val result = sut.findBestFit(200f, 300f)
        assertEquals(IntSize(200, 400), result)
    }

    @Test
    fun findBestFit_wideParent() {
        val sut = IntSize(100, 200)
        val result = sut.findBestFit(400f, 200f)
        assertEquals(IntSize(100, 200), result)
    }

    @Test
    fun findBestFit_withPadding() {
        val padding = 10F
        val sut = IntSize(100, 200)
        val result = sut.findBestFit(400f, 200f, padding)
        assertEquals(IntSize(80, 180), result)
    }
}