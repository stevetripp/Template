package com.example.template.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration

class AppColors(
    primary: Color,
    primaryVariant: Color,
    secondary: Color,
    secondaryVariant: Color,
    background: Color,
    surface: Color,
    error: Color,
    onPrimary: Color,
    onSecondary: Color,
    onBackground: Color,
    onSurface: Color,
    onError: Color,
    isLight: Boolean,
) {
    var primary by mutableStateOf(primary, structuralEqualityPolicy())
        internal set
    var primaryVariant by mutableStateOf(primaryVariant, structuralEqualityPolicy())
        internal set
    var secondary by mutableStateOf(secondary, structuralEqualityPolicy())
        internal set
    var secondaryVariant by mutableStateOf(secondaryVariant, structuralEqualityPolicy())
        internal set
    var background by mutableStateOf(background, structuralEqualityPolicy())
        internal set
    var surface by mutableStateOf(surface, structuralEqualityPolicy())
        internal set
    var error by mutableStateOf(error, structuralEqualityPolicy())
        internal set
    var onPrimary by mutableStateOf(onPrimary, structuralEqualityPolicy())
        internal set
    var onSecondary by mutableStateOf(onSecondary, structuralEqualityPolicy())
        internal set
    var onBackground by mutableStateOf(onBackground, structuralEqualityPolicy())
        internal set
    var onSurface by mutableStateOf(onSurface, structuralEqualityPolicy())
        internal set
    var onError by mutableStateOf(onError, structuralEqualityPolicy())
        internal set
    var isLight by mutableStateOf(isLight, structuralEqualityPolicy())
        internal set

    constructor(colors: Colors) : this(
        primary = colors.primary,
        primaryVariant = colors.primaryVariant,
        secondary = colors.secondary,
        secondaryVariant = colors.secondaryVariant,
        background = colors.background,
        surface = colors.surface,
        error = colors.error,
        onPrimary = colors.onPrimary,
        onSecondary = colors.onSecondary,
        onBackground = colors.onBackground,
        onSurface = colors.onSurface,
        onError = colors.onError,
        isLight = colors.isLight,
    )

    fun toMaterialColors(): Colors {
        return Colors(
            primary,
            primaryVariant,
            secondary,
            secondaryVariant,
            background,
            surface,
            error,
            onPrimary,
            onSecondary,
            onBackground,
            onSurface,
            onError,
            isLight
        )
    }
}

private val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No AppColorPalette provided")
}

object AppTheme {
    private const val LARGE_SCREEN_DP = 600
    private const val LOW_DPI = 320

    val colors: AppColors
        @Composable get() = LocalAppColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val isLargeScreen: Boolean
        @Composable get() {
            return LocalConfiguration.current.smallestScreenWidthDp >= LARGE_SCREEN_DP
        }

    val isLandscape: Boolean
        @Composable get() {
            return LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
        }

    val isLowDpi: Boolean
        @Composable get() {
            return LocalConfiguration.current.densityDpi <= LOW_DPI
        }
}

fun lightAppColors(): AppColors {
    return AppColors(lightColors())
}

fun darkAppColors(): AppColors {
    return AppColors(darkColors())
}

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val appColors = if (darkTheme) darkAppColors() else lightAppColors()

    CompositionLocalProvider(
        LocalAppColors provides appColors,
    ) {
        MaterialTheme(
            colors = appColors.toMaterialColors(),
            typography = Typography,
            shapes = Shapes,
            content = content,
        )
    }
}
