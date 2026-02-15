package com.example.vk_android_vkat.ui.explore

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.vk_android_vkat.R

/**
 * Модель фильтра для SearchToolBar
 *
 * @param label Ресурс строки (для локализации)
 * @param iconVector Векторная иконка (необязательная)
 */
data class FilterItem(
    @StringRes val label: Int,
    val iconVector: ImageVector? = null
)

/**
 * Фильтры по умолчанию
 * В Composable будем использовать stringResource и Icon()
 */
fun defaultFilters() = listOf(
    FilterItem(label = R.string.nearby, iconVector = Icons.Default.LocationOn),
    FilterItem(label = R.string.target),
    FilterItem(label = R.string.location),
    FilterItem(label = R.string.day_time),
    FilterItem(label = R.string.duration)
)
