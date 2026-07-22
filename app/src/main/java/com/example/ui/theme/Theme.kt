package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkTurquoiseColorScheme = darkColorScheme(
  primary = TurquoisePrimary,
  onPrimary = TurquoiseOnPrimary,
  primaryContainer = TurquoiseContainer,
  onPrimaryContainer = TurquoiseOnContainer,
  secondary = TealSecondary,
  onSecondary = TealOnSecondary,
  secondaryContainer = TealContainer,
  onSecondaryContainer = TealOnContainer,
  tertiary = MintTertiary,
  onTertiary = MintOnTertiary,
  tertiaryContainer = MintContainer,
  onTertiaryContainer = MintOnContainer,
  background = DarkBackground,
  onBackground = DarkOnBackground,
  surface = DarkSurface,
  onSurface = DarkOnSurface,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkOnSurfaceVariant,
  outline = DarkOutline
)

@Composable
fun HydroTrackTheme(
  darkTheme: Boolean = true, // Default to dark theme as requested
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = DarkTurquoiseColorScheme,
    typography = Typography,
    content = content
  )
}

