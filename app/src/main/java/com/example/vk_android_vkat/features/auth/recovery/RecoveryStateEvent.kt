package com.example.vk_android_vkat.features.auth.recovery

import com.example.vk_android_vkat.features.auth.AuthError

data class RecoveryState(
    val email: String = "",
    val isLoading: Boolean = false,
    val emailError: AuthError? = null,
    val isRecoverySent: Boolean = false
)

sealed class RecoveryEvent {
    data class EmailChanged(val email: String) : RecoveryEvent()
    object SendRecoveryClicked : RecoveryEvent()
    object LoginClicked : RecoveryEvent()
    object ClearErrors : RecoveryEvent()
}