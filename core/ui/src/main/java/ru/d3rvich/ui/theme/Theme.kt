package ru.d3rvich.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF7067FF),
    onPrimary = Color.White,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF7067FF),
    surfaceContainerHighest = Color.White,
    surfaceVariant = Color.White,
    onSurfaceVariant = Color(0xFF000000),
    onSurface = Color(0xFFBABABA)
)

@Composable
fun DailyQuizTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = view.context.findActivity().window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography
    ) {
        Surface(color = DarkColorScheme.background, content = content)
    }
}

private fun Context.findActivity(): Activity = when (this) {
    !is ContextWrapper -> throw IllegalArgumentException("Activity not found.")
    is Activity -> this
    else -> this.baseContext.findActivity()
}