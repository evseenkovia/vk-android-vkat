package com.example.vk_android_vkat.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.data.delayTime
import com.example.vk_android_vkat.data.mockEmail
import com.example.vk_android_vkat.data.mockPassword
import com.example.vk_android_vkat.ui.auth.AuthError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    sealed class LoginEffect {
        object LoginSuccess : LoginEffect()
        object GoToRegistration : LoginEffect()
        object GoToPasswordRecovery : LoginEffect()
    }

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.EmailChanged -> _state.update { it.copy(email = event.email, emailError = null) }
            is LoginEvent.PasswordChanged -> _state.update { it.copy(password = event.password, passwordError = null) }
            LoginEvent.ClearErrors -> _state.update { it.copy(emailError = null, passwordError = null) }
            LoginEvent.LoginClicked -> login()
            LoginEvent.RegisterClicked -> viewModelScope.launch { _effect.send(LoginEffect.GoToRegistration) }
            LoginEvent.ForgotPasswordClicked -> viewModelScope.launch { _effect.send(LoginEffect.GoToPasswordRecovery) }
        }
    }

    private fun login() {
        _state.update { it.copy(isLoading = true, emailError = null, passwordError = null) }

        viewModelScope.launch {
            delay(delayTime)

            val email = state.value.email
            val password = state.value.password

            val emailErr = when {
                email.isBlank() -> AuthError.EmailBlank
                !email.contains("@") -> AuthError.EmailInvalid
                email != mockEmail -> AuthError.EmailNotFound
                else -> null
            }

            val passwordErr = when {
                password.isBlank() -> AuthError.PasswordBlank
                email == mockEmail && password != mockPassword -> AuthError.PasswordInvalid
                else -> null
            }

            val hasErrors = emailErr != null || passwordErr != null

            _state.update {
                it.copy(
                    isLoading = false,
                    emailError = emailErr,
                    passwordError = passwordErr,
                    isUserLoggedIn = !hasErrors
                )
            }

            if(!hasErrors){
                _effect.send(LoginEffect.LoginSuccess)
            }
        }
    }
}
