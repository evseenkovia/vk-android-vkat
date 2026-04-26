package com.example.vk_android_vkat.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Общие настройки типографики
object TypographyDefaults {
    // ОСНОВНЫЕ НАСТРОЙКИ
    val fontFamily = FontFamily.Default             // Шрифт
    val titleFontWeight = FontWeight.SemiBold       // Толщина
    val bodyFontWeight = FontWeight.Normal
    val accentFontWeight = FontWeight.Medium

    // Базовые размеры
    val titleLargeSize = 22.sp
    val titleMediumSize = 18.sp
    val titleSmallSize = 16.sp

    val bodyLargeSize = 16.sp
    val bodyMediumSize = 14.sp
    val bodySmallSize = 12.sp

    val labelLargeSize = 14.sp
    val labelMediumSize = 12.sp
    val labelSmallSize = 11.sp

    // Межстрочные интервалы
    val lineHeightTitle = 28.sp
    val lineHeightBody = 24.sp
    val lineHeightSmall = 20.sp
}

// Наша типографика
val AppTypography = Typography(
    // Title Large - для заголовков экранов
    titleLarge = TextStyle(
        fontFamily = TypographyDefaults.fontFamily,
        fontWeight = TypographyDefaults.titleFontWeight,
        fontSize = TypographyDefaults.titleLargeSize,
        lineHeight = TypographyDefaults.lineHeightTitle,
        letterSpacing = 0.5.sp
    ),

    // Title Medium - для подзаголовков
    titleMedium = TextStyle(
        fontFamily = TypographyDefaults.fontFamily,
        fontWeight = TypographyDefaults.titleFontWeight,
        fontSize = TypographyDefaults.titleMediumSize,
        lineHeight = TypographyDefaults.lineHeightTitle,
        letterSpacing = 0.5.sp
    ),

    // Title Small - для небольших заголовков
    titleSmall = TextStyle(
        fontFamily = TypographyDefaults.fontFamily,
        fontWeight = TypographyDefaults.titleFontWeight,
        fontSize = TypographyDefaults.titleSmallSize,
        lineHeight = TypographyDefaults.lineHeightTitle,
        letterSpacing = 0.5.sp
    ),

    // Body Large - основной текст
    bodyLarge = TextStyle(
        fontFamily = TypographyDefaults.fontFamily,
        fontWeight = TypographyDefaults.bodyFontWeight,
        fontSize = TypographyDefaults.bodyLargeSize,
        lineHeight = TypographyDefaults.lineHeightBody,
        letterSpacing = 0.5.sp
    ),

    // Body Medium - второстепенный текст
    bodyMedium = TextStyle(
        fontFamily = TypographyDefaults.fontFamily,
        fontWeight = TypographyDefaults.bodyFontWeight,
        fontSize = TypographyDefaults.bodyMediumSize,
        lineHeight = TypographyDefaults.lineHeightBody,
        letterSpacing = 0.25.sp
    ),

    // Body Small - мелкий текст
    bodySmall = TextStyle(
        fontFamily = TypographyDefaults.fontFamily,
        fontWeight = TypographyDefaults.bodyFontWeight,
        fontSize = TypographyDefaults.bodySmallSize,
        lineHeight = TypographyDefaults.lineHeightSmall,
        letterSpacing = 0.4.sp
    ),

    // Label Large - для кнопок
    labelLarge = TextStyle(
        fontFamily = TypographyDefaults.fontFamily,
        fontWeight = TypographyDefaults.accentFontWeight,
        fontSize = TypographyDefaults.labelLargeSize,
        lineHeight = TypographyDefaults.lineHeightSmall,
        letterSpacing = 0.1.sp
    ),

    // Label Medium - для подписей
    labelMedium = TextStyle(
        fontFamily = TypographyDefaults.fontFamily,
        fontWeight = TypographyDefaults.accentFontWeight,
        fontSize = TypographyDefaults.labelMediumSize,
        lineHeight = TypographyDefaults.lineHeightSmall,
        letterSpacing = 0.5.sp
    ),

    // Label Small - для очень мелких подписей
    labelSmall = TextStyle(
        fontFamily = TypographyDefaults.fontFamily,
        fontWeight = TypographyDefaults.accentFontWeight,
        fontSize = TypographyDefaults.labelSmallSize,
        lineHeight = TypographyDefaults.lineHeightSmall,
        letterSpacing = 0.5.sp
    ),

    // Для крупных заголовков
    displayLarge = TextStyle(
        fontFamily = TypographyDefaults.fontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),

    displayMedium = TextStyle(
        fontFamily = TypographyDefaults.fontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),

    displaySmall = TextStyle(
        fontFamily = TypographyDefaults.fontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),

    // Для заголовков
    headlineLarge = TextStyle(
        fontFamily = TypographyDefaults.fontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = TypographyDefaults.fontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = TypographyDefaults.fontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 24.sp,
        lineHeight = 32.sp
    )
)

// Светлая тема
val AppLightColorScheme = lightColorScheme(
    primary = Color(0xFFD28B5E),
    onPrimary = Color(0xFF3E2F2C),
    secondary = Color(0xFFFAEBDD),
    onSecondary = Color(0xFF3E2F2C),
    background = Color(0xFFFFF9F3),
    onBackground = Color(0xFF3E2F2C),
    surface = Color.White,
    onSurface = Color.Black,
    error = Color(0xFFB00020),
    onError = Color.White,

)

// Темная тема
val AppDarkColorScheme = darkColorScheme(
    primary = Color(0xFFB97A50),
    onPrimary = Color(0xFF2B1F1C),

    secondary = Color(0xFF3E2F2C),
    onSecondary = Color(0xFFF5E6D8),

    background = Color(0xFF1A1412),
    onBackground = Color(0xFFEDE0D4),

    surface = Color(0xFF241B18),
    onSurface = Color(0xFFEDE0D4),

    error = Color(0xFFCF6679),
    onError = Color(0xFF1B0000)
)

//val navigationBarItemColors = NavigationBarItemColors(
//    selectedIconColor = AppLightColorScheme.primary,
//    selectedTextColor = AppLightColorScheme.primary,
//    selectedIndicatorColor = Color.Transparent,
//    unselectedIconColor = AppLightColorScheme.onBackground,
//    unselectedTextColor = AppLightColorScheme.onBackground,
//    disabledIconColor = AppLightColorScheme.onBackground,
//    disabledTextColor = AppLightColorScheme.onBackground
//)

// Композиционная тема приложения
@Composable
fun MyMapsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) AppDarkColorScheme else AppLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )

}