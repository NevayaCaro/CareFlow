package com.nevaya.careflow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light theme color scheme
private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,    // Primary button color
    onPrimary = Color.White,    // Text on primary button

    secondary = GreenDark,     // Back button color
    onSecondary = Color.White,  // Text on back button

    background = AppBackground, // App background
    onBackground = TextPrimary, // Text color on background

    surface = CardBackground,   // Cards or surfaces
    onSurface = TextPrimary     // Text color on cards
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