package com.example.vk_android_vkat.ui.auth.login

import com.example.vk_android_vkat.ui.auth.AuthError

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: AuthError? = null,
    val passwordError: AuthError? = null,
    val isUserLoggedIn: Boolean = false
)

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object LoginClicked : LoginEvent() // Основное действие

    object RegisterClicked : LoginEvent()
    object ForgotPasswordClicked : LoginEvent()
    object ClearErrors : LoginEvent()
}
