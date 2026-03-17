package com.example.vk_android_vkat.common.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.vk_android_vkat.R

/**
 * Базовое поле ввода с поддержкой ошибок и иконок
 */
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    placeholderText: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    errorMessage: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    shape: Shape = AppTextFieldDefaults.textFieldShape,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isPassword: Boolean = false
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) },
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        imageVector = it,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = when {
                trailingIcon != null && onTrailingIconClick != null -> {
                    {
                        IconButton(onClick = onTrailingIconClick) {
                            Icon(
                                imageVector = trailingIcon,
                                contentDescription = null
                            )
                        }
                    }
                }
                isPassword -> {
                    val icon = if (visualTransformation is PasswordVisualTransformation)
                        Icons.Outlined.Visibility
                    else
                        Icons.Outlined.VisibilityOff

                    {
                        IconButton(onClick = { onTrailingIconClick?.invoke() }) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null
                            )
                        }
                    }
                }
                else -> null
            },
            placeholder = { Text(placeholderText) },
            isError = errorMessage != null,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = { onImeAction() },
                onNext = { onImeAction() }
            ),
            visualTransformation = if (isPassword) {
                if ((trailingIcon != null && onTrailingIconClick != null) || trailingIcon == null) {

                    if (trailingIcon == Icons.Filled.Visibility)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation()
                } else {
                    PasswordVisualTransformation()
                }
            } else {
                VisualTransformation.None
            },
            shape = shape,
            colors = AppTextFieldDefaults.textFieldColors()
        )

        // Ошибка с фиксированной высотой, но не сжимающая поле
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

/**
 * Поле для ввода email
 */
@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Email",
    errorMessage: String? = null,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        leadingIcon = Icons.Outlined.Email,
        errorMessage = errorMessage,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onImeAction = onImeAction
    )
}

/**
 * Поле для ввода пароля
 */
@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Пароль",
    errorMessage: String? = null,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    var passwordVisible by remember { mutableStateOf(false) }

    AppTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        leadingIcon = Icons.Outlined.Lock,
        trailingIcon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
        onTrailingIconClick = { passwordVisible = !passwordVisible },
        errorMessage = errorMessage,
        enabled = enabled,
        isPassword = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        imeAction = imeAction,
        onImeAction = onImeAction
    )
}

/**
 * Поле для ввода имени
 */
@Composable
fun NameField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = stringResource(R.string.name),
        leadingIcon = Icons.Outlined.Person,
        errorMessage = errorMessage,
        enabled = enabled,
        keyboardType = KeyboardType.Text,
        imeAction = imeAction,
        onImeAction = onImeAction
    )
}

/**
 * Поле для ввода телефона
 */
//@Composable
//fun PhoneField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    modifier: Modifier = Modifier,
//    errorMessage: String? = null,
//    enabled: Boolean = true,
//    imeAction: ImeAction = ImeAction.Next,
//    onImeAction: () -> Unit = {}
//) {
//    AppTextField(
//        value = value,
//        onValueChange = onValueChange,
//        modifier = modifier,
//        label = stringResource(R.string.phone),
//        leadingIcon = Icons.Outlined.Phone,
//        errorMessage = errorMessage,
//        enabled = enabled,
//        keyboardType = KeyboardType.Phone,
//        imeAction = imeAction,
//        onImeAction = onImeAction
//    )
//}

/**
 * Поле для ввода текста (многострочное)
 */
@Composable
fun MultilineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    errorMessage: String? = null,
    enabled: Boolean = true,
    maxLines: Int = 5
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        errorMessage = errorMessage,
        enabled = enabled,
        singleLine = false,
        maxLines = maxLines,
        keyboardType = KeyboardType.Text
    )
}

/**
 * Поле для поиска
 */
@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
){
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        textStyle = AppTypography.bodyLarge,
        placeholder = { Text("Поиск...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Поиск"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Очистить поиск"
                    )
                }
            }
        },
        singleLine = true,
        colors = AppTextFieldDefaults.textFieldColors()
    )
}