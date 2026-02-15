package com.example.vk_android_vkat.ui.auth

sealed class AuthError {
    object EmailBlank : AuthError()
    object EmailInvalid : AuthError()
    object EmailNotFound : AuthError()
    object PasswordBlank : AuthError()
    object PasswordInvalid : AuthError()
    object PasswordTooShort : AuthError()
    object PasswordsMismatch : AuthError()
    data class Custom(val message: String) : AuthError()
}