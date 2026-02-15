package com.example.vk_android_vkat.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit,
    onLoginSuccess: () -> Unit = {},
    onNavigate: (AuthMode) -> Unit
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
            AuthMode.Login -> LoginScreen(state, onEvent, onNavigate, onLoginSuccess)
            AuthMode.Registration -> RegistrationScreen(state, onEvent, onNavigate, onLoginSuccess)
            AuthMode.PasswordRecovery -> PasswordRecoveryScreen(state, onEvent, onNavigate)
        }
    }
}