package com.example.vk_android_vkat.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(
    state: AuthState,
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit = {}
) {
    // Следим за успешным логином
    LaunchedEffect(state.isUserLoggedIn) {
        if (state.isUserLoggedIn) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // Подгружаем форму в зависимости от действия
        when (state.mode) {
            AuthMode.Login -> LoginForm(state, viewModel)
            AuthMode.Register -> RegisterForm(state, viewModel)
            AuthMode.ForgotPassword -> ForgotPasswordForm(state, viewModel)
        }
    }
}

@Composable
fun LoginForm(
    state: AuthState,
    viewModel: LoginViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Основной контент
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Заголовок
            Text(
                text = "Вход",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.email,
                onValueChange = viewModel::updateEmail,
                label = { Text("Email") },
                singleLine = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email"
                    )
                },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            var isPasswordVisible by remember { mutableStateOf(false) }

            // Пароль
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                label = { Text("Пароль") },
                value = state.password,
                onValueChange = viewModel::updatePassword,
                singleLine = true,
                visualTransformation = if (isPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible)
                                Icons.Filled.VisibilityOff
                            else
                                Icons.Filled.Visibility,
                            contentDescription = "Visibility icon"
                        )
                    }
                }
            )

            // Ошибка если есть
            if (state.error != null) {
                Text(
                    text = state.error,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка входа
            Button(
                onClick = viewModel::login,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.email.isNotBlank() && state.password.isNotBlank(),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Войти")
                }
            }

            // "Забыли пароль?" по центру
            TextButton(
                onClick = { viewModel.setMode(AuthMode.ForgotPassword) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Забыли пароль?")
            }
        }

        // Кнопка регистрации внизу
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Нет аккаунта?",
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = { viewModel.setMode(AuthMode.Register) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Зарегистрироваться")
            }
        }
    }
}


@Composable
fun RegisterForm(
    state: AuthState,
    viewModel: LoginViewModel
) {
    Column(
        modifier = Modifier.
            fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Заголовок
            Text(
                text = "Регистрация",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.email,
                onValueChange = viewModel::updateEmail,
                label = { Text("Email") },
                singleLine = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email"
                    )
                },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            var isPasswordVisible by remember { mutableStateOf(false) }

            // Пароль
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                label = { Text("Пароль") },
                value = state.password,
                onValueChange = viewModel::updatePassword,
                singleLine = true,
                visualTransformation = if (isPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible)
                                Icons.Filled.VisibilityOff
                            else
                                Icons.Filled.Visibility,
                            contentDescription = "Visibility icon"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            var isPasswordConfirmVisible by remember { mutableStateOf(false) }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                label = { Text("Повторите пароль") },
                value = state.confirmPassword,
                onValueChange = { viewModel.updateConfirmPassword(it) },
                singleLine = true,
                visualTransformation = if (isPasswordConfirmVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordConfirmVisible = !isPasswordConfirmVisible }) {
                        Icon(
                            imageVector = if (isPasswordConfirmVisible)
                                Icons.Filled.VisibilityOff
                            else
                                Icons.Filled.Visibility,
                            contentDescription = "Visibility icon"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = viewModel::register,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.email.isNotBlank() &&
                        state.password.isNotBlank() &&
                        state.password == state.confirmPassword
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Зарегистрироваться")
                }
            }
        }

        // Возврат к авторизации
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Уже есть аккаунт?",
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = { viewModel.setMode(AuthMode.Login) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Войти")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordForm(
    state: AuthState = AuthState(mode = AuthMode.ForgotPassword),
    viewModel: LoginViewModel = LoginViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок
        Text(
            text = "Восстановление пароля",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Описание
        Text(
            text = "Введите email для восстановления пароля",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Поле email
        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::updateEmail,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                Icon(imageVector = Icons.Filled.Email,
                    contentDescription = "Email icon")
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка отправки
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = state.email.isNotBlank()
        ) {
            Text("Отправить ссылку")
        }

        // Кнопка возврата
        TextButton(
            onClick = { viewModel.setMode(AuthMode.Login) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Вернуться к входу")
        }
    }
}