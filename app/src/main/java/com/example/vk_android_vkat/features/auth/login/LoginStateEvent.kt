package com.example.vk_android_vkat.features.auth.login

import com.example.vk_android_vkat.features.auth.AuthError
import com.vk.id.AccessToken

// Описывает состояние экрана
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: AuthError? = null,
    val passwordError: AuthError? = null,
    val isUserLoggedIn: Boolean = false,
    val isVKAuthInProgress: Boolean = false,
    val vkAuthError: String? = null
)

// Действия пользователя
sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object LoginClicked : LoginEvent() // Основное действие

    object RegisterClicked : LoginEvent()
    object ForgotPasswordClicked : LoginEvent()
    // VK ID Auth
    data class VKAuthSuccess(val accessToken: AccessToken) : LoginEvent()
    data class VKAuthError(val error: String) : LoginEvent()
}
