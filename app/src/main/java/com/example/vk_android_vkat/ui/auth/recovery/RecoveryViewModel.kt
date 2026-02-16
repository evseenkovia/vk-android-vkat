package com.example.vk_android_vkat.ui.auth.recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.data.mockEmail
import com.example.vk_android_vkat.ui.auth.AuthError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecoveryViewModel : ViewModel() {

    sealed class RecoveryEffect {
        object GoToLogin : RecoveryEffect()
        object EmailSent : RecoveryEffect()
    }

    private val _effect = Channel<RecoveryEffect>()
    val effect = _effect.receiveAsFlow()

    private val _state = MutableStateFlow(RecoveryState())
    val state = _state.asStateFlow()

    fun onEvent(event: RecoveryEvent) {
        when(event) {
            is RecoveryEvent.EmailChanged -> _state.update { it.copy(email = event.email, emailError = null) }
            RecoveryEvent.ClearErrors -> _state.update { it.copy(emailError = null) }
            RecoveryEvent.SendRecoveryClicked -> sendRecovery()
            RecoveryEvent.LoginClicked -> viewModelScope.launch { _effect.send(
                RecoveryEffect.GoToLogin) }
        }
    }

    private fun sendRecovery() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            delay(2000)

            val email = state.value.email

            val emailErr = when {
                email.isBlank() -> AuthError.EmailBlank
                email != mockEmail -> AuthError.EmailNotFound
                else -> null
            }

            _state.update {
                it.copy(
                    isLoading = false,
                    emailError = emailErr,
                    isRecoverySent = emailErr == null
                )
            }

            val hasErrors = emailErr != null

            if (!hasErrors){
                //todo -> реализовать отправку email на почту
                _effect.send(RecoveryEffect.EmailSent)
            }
        }
    }
}
