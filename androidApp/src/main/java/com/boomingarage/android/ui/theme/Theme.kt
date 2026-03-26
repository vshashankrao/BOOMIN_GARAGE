package com.boomingarage.android.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Orange500,
    onPrimary = White,
    primaryContainer = Orange100,
    onPrimaryContainer = Orange700,
    secondary = Charcoal800,
    onSecondary = White,
    secondaryContainer = Charcoal100,
    onSecondaryContainer = Charcoal900,
    tertiary = Teal500,
    onTertiary = White,
    background = OffWhite,
    onBackground = Charcoal900,
    surface = White,
    onSurface = Charcoal900,
    surfaceVariant = LightGray,
    onSurfaceVariant = DarkGray,
    error = StatusRed,
    onError = White
)

private val DarkColorScheme = darkColorScheme(
    primary = Orange500,
    onPrimary = White,
    primaryContainer = Orange700,
    onPrimaryContainer = Orange100,
    secondary = Charcoal100,
    onSecondary = Charcoal900,
    secondaryContainer = Charcoal700,
    onSecondaryContainer = Charcoal100,
    tertiary = Teal400,
    onTertiary = Charcoal900,
    background = Charcoal900,
    onBackground = White,
    surface = Charcoal800,
    onSurface = White,
    surfaceVariant = DarkGray,
    onSurfaceVariant = LightGray,
    error = StatusRed,
    onError = White
)

@Composable
fun BoominGarageTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
