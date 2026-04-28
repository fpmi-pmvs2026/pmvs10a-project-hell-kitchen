package com.mealhunter.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

// Colors
val Orange500 = Color(0xFFFF6D00)
val Orange700 = Color(0xFFE65100)
val Green500 = Color(0xFF4CAF50)
val Red500 = Color(0xFFF44336)
val Grey50 = Color(0xFFFAFAFA)
val Grey100 = Color(0xFFF5F5F5)
val Grey800 = Color(0xFF424242)
val Grey900 = Color(0xFF212121)

private val LightColorScheme = lightColorScheme(
    primary = Orange500,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0B2),
    onPrimaryContainer = Orange700,
    secondary = Green500,
    onSecondary = Color.White,
    background = Grey50,
    onBackground = Grey900,
    surface = Color.White,
    onSurface = Grey900,
    surfaceVariant = Grey100,
    error = Red500,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = Orange500,
    onPrimary = Color.White,
    primaryContainer = Orange700,
    onPrimaryContainer = Color(0xFFFFE0B2),
    secondary = Green500,
    onSecondary = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2C2C2C),
    error = Color(0xFFCF6679),
    onError = Color.Black
)

@Composable
fun MealHunterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(
            headlineLarge = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 36.sp
            ),
            headlineMedium = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 28.sp
            ),
            titleLarge = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 26.sp
            ),
            titleMedium = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 22.sp
            ),
            bodyLarge = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp
            ),
            bodyMedium = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            labelLarge = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        ),
        content = content
    )
}
