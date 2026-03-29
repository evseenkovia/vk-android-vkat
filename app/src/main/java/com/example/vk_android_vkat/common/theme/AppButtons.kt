package com.example.vk_android_vkat.common.theme


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


object AppButtonDefaults {
    // Общие настройки для всех кнопок
    val buttonShape = RoundedCornerShape(12.dp)
    val buttonHeight = 56.dp
    val smallButtonHeight = 40.dp
    val iconButtonSize = 48.dp

    // Цвета для разных состояний
    @Composable
    fun primaryButtonColors() = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
    )

    @Composable
    fun secondaryButtonColors() = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
        disabledContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f)
    )

    @Composable
    fun outlineButtonColors() = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.primary,
        disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    )

    @Composable
    fun errorButtonColors() = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.onError,
        disabledContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
        disabledContentColor = MaterialTheme.colorScheme.onError.copy(alpha = 0.5f)
    )
}

// ============= ОСНОВНЫЕ КНОПКИ =============

/**
 * Основная кнопка (залитая цветом primary)
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    fullWidth: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .height(AppButtonDefaults.buttonHeight),
        enabled = enabled && !isLoading,
        shape = AppButtonDefaults.buttonShape,
        colors = AppButtonDefaults.primaryButtonColors(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (isLoading) {
            // Можно добавить индикатор загрузки
            androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(8.dp))
            }
            Text(
                text = text,
                style = AppTextStyles.buttonText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Вторичная кнопка (залитая цветом secondary)
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fullWidth: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .height(AppButtonDefaults.buttonHeight),
        enabled = enabled,
        shape = AppButtonDefaults.buttonShape,
        colors = AppButtonDefaults.secondaryButtonColors()
    ) {
        Text(
            text = text,
            style = AppTextStyles.buttonText
        )
    }
}

/**
 * Кнопка с обводкой
 */
@Composable
fun OutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fullWidth: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .height(AppButtonDefaults.buttonHeight),
        enabled = enabled,
        shape = AppButtonDefaults.buttonShape,
        colors = AppButtonDefaults.outlineButtonColors()
    ) {
        Text(
            text = text,
            style = AppTextStyles.buttonText
        )
    }
}

/**
 * Текстовая кнопка (без фона)
 */
@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colorScheme.primary
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = AppButtonDefaults.buttonShape
    ) {
        Text(
            text = text,
            style = AppTextStyles.buttonText,
            color = color
        )
    }
}

/**
 * Кнопка с заливкой тональной (тонированная)
 */
@Composable
fun TonalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fullWidth: Boolean = true
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .height(AppButtonDefaults.buttonHeight),
        enabled = enabled,
        shape = AppButtonDefaults.buttonShape
    ) {
        Text(
            text = text,
            style = AppTextStyles.buttonText
        )
    }
}

/**
 * Кнопка ошибки (красная)
 */
@Composable
fun ErrorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fullWidth: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .height(AppButtonDefaults.buttonHeight),
        enabled = enabled,
        shape = AppButtonDefaults.buttonShape,
        colors = AppButtonDefaults.errorButtonColors()
    ) {
        Text(
            text = text,
            style = AppTextStyles.buttonText
        )
    }
}

/**
 * Маленькая кнопка (для компактных интерфейсов)
 */
@Composable
fun SmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(AppButtonDefaults.smallButtonHeight),
        enabled = enabled,
        shape = AppButtonDefaults.buttonShape,
        colors = AppButtonDefaults.primaryButtonColors(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(4.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1
        )
    }
}

// ============= ИКОНОЧНЫЕ КНОПКИ =============

/**
 * Основная иконочная кнопка
 */
@Composable
fun AppIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    enabled: Boolean = true,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(AppButtonDefaults.iconButtonSize),
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

/**
 * Иконочная кнопка с фоном
 */
@Composable
fun FilledIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    tint: Color = MaterialTheme.colorScheme.onPrimary
) {
    androidx.compose.material3.IconButton(
        onClick = onClick,
        modifier = modifier
            .size(AppButtonDefaults.iconButtonSize)
            .background(
                color = backgroundColor.copy(alpha = if (enabled) 1f else 0.5f),
                shape = AppButtonDefaults.buttonShape
            ),
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint.copy(alpha = if (enabled) 1f else 0.5f)
        )
    }
}

// ============= FAB КНОПКИ =============

/**
 * Floating Action Button с текстом
 */
@Composable
fun AppFloatingActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    expanded: Boolean = true
) {
    if (expanded) {
        androidx.compose.material3.FloatingActionButton(
            onClick = onClick,
            modifier = modifier
                .height(AppButtonDefaults.buttonHeight),
            shape = AppButtonDefaults.buttonShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(8.dp))
            }
            Text(
                text = text,
                style = AppTextStyles.buttonText,
                textAlign = TextAlign.Center
            )
        }
    } else {
        androidx.compose.material3.FloatingActionButton(
            onClick = onClick,
            modifier = modifier.size(56.dp),
            shape = AppButtonDefaults.buttonShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonExamples() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PrimaryButton(
            text = "Основная кнопка",
            onClick = {}
        )

        SecondaryButton(
            text = "Вторичная кнопка",
            onClick = {}
        )

        OutlineButton(
            text = "Кнопка с обводкой",
            onClick = {}
        )

        TextButton(
            text = "Текстовая кнопка",
            onClick = {}
        )

        ErrorButton(
            text = "Кнопка ошибки",
            onClick = {}
        )

        // Кнопка с иконкой и загрузкой
        PrimaryButton(
            text = "Загрузить",
            onClick = {},
            icon = Icons.Default.Download,
            isLoading = true
        )
    }
}