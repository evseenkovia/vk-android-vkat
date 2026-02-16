package com.example.vk_android_vkat.ui.auth.registration

import com.example.vk_android_vkat.ui.auth.AuthError

data class RegistrationState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val emailError: AuthError? = null,
    val passwordError: AuthError? = null,
    val confirmPasswordError: AuthError? = null,
    val isRegistered: Boolean = false
)

sealed class RegistrationEvent {
    data class EmailChanged(val email: String) : RegistrationEvent()
    data class PasswordChanged(val password: String) : RegistrationEvent()
    data class ConfirmPasswordChanged(val confirm: String) : RegistrationEvent()
    object RegisterClicked : RegistrationEvent() // Основное действие

    object LoginClicked : RegistrationEvent()
    object ClearErrors : RegistrationEvent()
}