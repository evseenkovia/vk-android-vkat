package com.example.vk_android_vkat.common.theme


import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

object AppTextFieldDefaults {
    // Общие настройки
    val textFieldShape = RoundedCornerShape(12.dp)
    val textFieldHeight = 56.dp
    val errorTextHeight = 20.dp

    // Цвета для разных состояний
    @Composable
    fun textFieldColors() = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
        errorBorderColor = MaterialTheme.colorScheme.error,

        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        errorLabelColor = MaterialTheme.colorScheme.error,

        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        errorLeadingIconColor = MaterialTheme.colorScheme.error,

        focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        errorTrailingIconColor = MaterialTheme.colorScheme.error,

        cursorColor = MaterialTheme.colorScheme.primary,
        errorCursorColor = MaterialTheme.colorScheme.error
    )
}