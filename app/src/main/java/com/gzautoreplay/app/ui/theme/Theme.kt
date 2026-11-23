package com.gzautoreplay.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Identidade visual inicial do GZ AutoReplay: verde-azulado (teal) + âmbar.
private val GzTeal = Color(0xFF0F766E)
private val GzTealLight = Color(0xFF5EEAD4)
private val GzAmber = Color(0xFFF59E0B)

private val LightColors = lightColorScheme(
    primary = GzTeal,
    onPrimary = Color.White,
    secondary = GzAmber,
    onSecondary = Color(0xFF1F1300),
    background = Color(0xFFF6FAF9),
    onBackground = Color(0xFF0B1F1D),
    surface = Color.White,
    onSurface = Color(0xFF0B1F1D),
    surfaceVariant = Color(0xFFDCEEEB),
    onSurfaceVariant = Color(0xFF30504C),
)

private val DarkColors = darkColorScheme(
    primary = GzTealLight,
    onPrimary = Color(0xFF00201D),
    secondary = GzAmber,
    onSecondary = Color(0xFF1F1300),
    background = Color(0xFF0B1514),
    onBackground = Color(0xFFE2F1EE),
    surface = Color(0xFF111E1D),
    onSurface = Color(0xFFE2F1EE),
    surfaceVariant = Color(0xFF1C3230),
    onSurfaceVariant = Color(0xFFA9C7C3),
)

@Composable
fun GZAutoReplayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
