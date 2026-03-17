package com.example.vk_android_vkat.common.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

object AppTextStyles {
    // Заголовок маршрута (жирный)
    val routeTitle @Composable get() = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold
    )

    // Статистика маршрута (дистанция, время)
    val routeStats @Composable get() = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    // Рейтинг маршрута
    val routeRating @Composable get() = MaterialTheme.typography.bodyLarge.copy(
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.primary
    )

    // Описание маршрута
    val routeDescription @Composable get() = MaterialTheme.typography.bodyLarge.copy(
        lineHeight = 24.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    // Заголовок секции
    val sectionTitle @Composable get() = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.SemiBold
    )

    // Текст на кнопке
    val buttonText @Composable get() = MaterialTheme.typography.labelLarge.copy(
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.5.sp
    )

    // Мелкий текст (подписи)
    val captionText @Composable get() = MaterialTheme.typography.bodySmall.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun TitleText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge,
        color = color,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Composable
fun BodyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = color,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Composable
fun SmallText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        color = color,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun LabelText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelLarge,
        color = color
    )
}

@Composable
fun HeadingText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.headlineMedium,
        color = color
    )
}

@Composable
fun RouteTitleText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = AppTextStyles.routeTitle,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun RouteStatsText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = AppTextStyles.routeStats
    )
}