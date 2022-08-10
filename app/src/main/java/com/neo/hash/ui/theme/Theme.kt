package com.neo.hash.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = PurpleLight,
    primaryVariant = PurpleDark,
)

private val LightColorPalette = lightColors(
    primary = PurpleDark,
    primaryVariant = PurpleLight,
)

@Composable
fun HashTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun HashBackground(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.background,
    content: @Composable () -> Unit,
) = Surface(
    modifier = modifier,
    color = color,
    content = content
)