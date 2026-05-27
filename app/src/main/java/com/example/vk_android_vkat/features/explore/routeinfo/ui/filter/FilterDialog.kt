package com.example.vk_android_vkat.features.explore.routeinfo.ui.filter

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.vk_android_vkat.common.theme.AppLightColorScheme
import com.example.vk_android_vkat.common.theme.AppTypography
import com.example.vk_android_vkat.common.theme.OutlineButton
import com.example.vk_android_vkat.common.theme.PrimaryButton
import com.example.vk_android_vkat.features.explore.domain.filter.RouteFilter
import kotlin.math.round



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    filters: RouteFilter,
    onFiltersChanged: (RouteFilter) -> Unit,
    onClearFilters: () -> Unit,
    onApply: () -> Unit

) {
    if (showDialog) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            modifier = Modifier.fillMaxHeight(),
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 4.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .then(
                        // Принудительно обновляем при изменении filters
                        if (filters != RouteFilter()) {
                            Modifier
                        } else {
                            Modifier
                        }
                    ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                FilterItem(
                    fieldName = "Рейтинг",
                    imageVector = Icons.Outlined.StarBorder,
                    valueRange = 0f..5f,
                    step = 0.5f,
                    currentRange = filters.rating,
                    onRangeChange = { newRange ->
                        onFiltersChanged(filters.copy(rating = newRange))
                    }
                )
                FilterItem(
                    fieldName = "Дистанция (км)",
                    imageVector = Icons.Outlined.Timeline,
                    valueRange = 0f..50f,
                    step = 1f,
                    currentRange = filters.distance,
                    onRangeChange = { newRange ->
                        onFiltersChanged(filters.copy(distance = newRange))
                    }
                )
                FilterItem(
                    fieldName = "Время (ч)",
                    imageVector = Icons.Outlined.AccessTime,
                    valueRange = 0f..8f,
                    step = 0.5f,
                    currentRange = filters.duration,
                    onRangeChange = { newRange ->
                        onFiltersChanged(filters.copy(duration = newRange))
                    }
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlineButton(
                        text = "Сбросить",
                        onClick = onClearFilters,
                        modifier = Modifier.weight(1f)
                    )
                    PrimaryButton(
                        text = "Применить",
                        onClick = {
                            onApply()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun FilterItem(
    fieldName: String,
    imageVector: ImageVector,
    valueRange: ClosedFloatingPointRange<Float>,
    step: Float,
    currentRange: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column(

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = imageVector, contentDescription = fieldName
            )
            Text(
                text = fieldName,
                style = AppTypography.titleSmall
            )
        }
        FilterRangeSlider(
            valueRange = valueRange,
            step = step,
            currentRange = currentRange,
            onRangeChange = onRangeChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterRangeSlider(
    valueRange: ClosedFloatingPointRange<Float>,
    step: Float,
    currentRange: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    val roundToStep: (Float, Float) -> Float = {
        value, step -> (round(value / step) * step)
    }

    Column {
        val startInteractionSource = remember { MutableInteractionSource() }
        val endInteractionSource = remember { MutableInteractionSource() }

        RangeSlider(
            value = currentRange,
            onValueChange = { range ->
                val roundedStart = roundToStep(range.start, step)
                val roundedEnd = roundToStep(range.endInclusive, step)
                onRangeChange(roundedStart..roundedEnd)
            },
            valueRange = valueRange,
            steps = ((valueRange.endInclusive - valueRange.start) / step).toInt() - 1,
            colors = SliderDefaults.colors(
                activeTrackColor = AppLightColorScheme.primary,
                activeTickColor = AppLightColorScheme.onPrimary,
                inactiveTrackColor = AppLightColorScheme.secondary,
                inactiveTickColor = AppLightColorScheme.onSecondary,
                thumbColor = AppLightColorScheme.primary
            ),
            startInteractionSource = startInteractionSource,
            endInteractionSource = endInteractionSource,
            startThumb = {
                SliderDefaults.Thumb(
                    interactionSource = startInteractionSource,
                    colors = SliderDefaults.colors(thumbColor = AppLightColorScheme.primary),
                    enabled = true
                )
            },
            endThumb = {
                SliderDefaults.Thumb(
                    interactionSource = endInteractionSource,
                    colors = SliderDefaults.colors(thumbColor = AppLightColorScheme.primary),
                    enabled = true
                )
            }
        )
        // Индикаторы под слайдером
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ValueChip(value = currentRange.start)
            ValueChip(value = currentRange.endInclusive)
        }
    }
}

@Composable
fun ValueChip(value: Float) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = AppLightColorScheme.primary.copy(alpha = 0.1f),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = value.toString(),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = AppLightColorScheme.primary
        )
    }
}