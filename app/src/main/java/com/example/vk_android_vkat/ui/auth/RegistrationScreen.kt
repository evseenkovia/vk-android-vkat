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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vk_android_vkat.R

@Preview(showBackground = true)
@Composable
fun RegisterFormPreview(){
    RegistrationScreen(
        state = AuthState(mode = AuthMode.Registration),
        onEvent = {},
        onRegisterSuccess = {},
        onNavigate = {}
    )
}

@Composable
fun RegistrationScreen(
    state: AuthState,
    onEvent : (AuthEvent) -> Unit,
    onNavigate: (AuthMode) -> Unit,
    onRegisterSuccess: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Заголовок
            Text(
                text = stringResource(R.string.registration),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.email,
                onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
                label = { Text(stringResource(R.string.email)) },
                singleLine = true,
                isError = state.emailError != null,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = stringResource(R.string.email)
                    )
                },
                shape = RoundedCornerShape(12.dp)
            )
            state.emailError?.let {
                Text(
                    text = stringResource(it),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            var isPasswordVisible by remember { mutableStateOf(false) }

            // Пароль
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                label = { Text(stringResource(R.string.password)) },
                value = state.password,
                onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
                singleLine = true,
                isError = state.passwordError != null,
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
            state.passwordError?.let {
                Text(
                    text = (stringResource(it)),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            var isPasswordConfirmVisible by remember { mutableStateOf(false) }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                label = { Text(stringResource(R.string.repeat_password)) },
                value = state.confirmPassword,
                isError = state.confirmPasswordError != null,
                onValueChange = { onEvent(AuthEvent.ConfirmPasswordChanged(it)) },
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
            state.confirmPasswordError?.let {
                Text(
                    text = stringResource(it),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onEvent(AuthEvent.RegisterClicked) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.email.isNotBlank() &&
                        state.password.isNotBlank() &&
                        state.confirmPassword.isNotBlank()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(stringResource(R.string.sign_up))
                }
            }
        }

        // Возврат к авторизации
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.already_have_an_account),
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = { onNavigate(AuthMode.Login) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(R.string.sign_in_do))
            }

        }
    }
}