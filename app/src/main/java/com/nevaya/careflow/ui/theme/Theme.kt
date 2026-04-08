package com.nevaya.careflow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light theme color scheme
private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.Green,

    secondary = GreenDark,
    onSecondary = Color.Gray,

    background = AppBackground,
    onBackground = TextPrimary,

    surface = CardBackground,
    onSurface = TextPrimary
)

@Composable
fun CareFlowTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}