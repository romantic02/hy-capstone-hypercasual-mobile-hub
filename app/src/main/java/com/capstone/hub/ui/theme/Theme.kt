package com.capstone.hub.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = HubColors.HeaderRed,
    onPrimary = Color.White,
    secondary = HubColors.FrameYellow,
    background = HubColors.OuterBackground,
    surface = HubColors.PanelWhite,
    onBackground = HubColors.BorderBlack,
    onSurface = HubColors.BorderBlack,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9FA8DA),
    onPrimary = Color(0xFF1A237E),
    secondary = Color(0xFF7986CB),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
)

@Composable
fun CapstoneHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
