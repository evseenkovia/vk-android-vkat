package com.example.vk_android_vkat.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.data.delayTime
import com.example.vk_android_vkat.data.mockEmail
import com.example.vk_android_vkat.data.mockPassword
import com.example.vk_android_vkat.features.auth.AuthError
import com.example.vk_android_vkat.features.auth.data.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    val authRepository: AuthRepository
) : ViewModel() {

    sealed class LoginEffect {
        object LoginSuccess : LoginEffect()
        object GoToRegistration : LoginEffect()
        object GoToPasswordRecovery : LoginEffect()
    }

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    // функция определяет
    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> _state.update {
                it.copy(
                    email = event.email,
                    emailError = null
                )
            }

            is LoginEvent.PasswordChanged -> _state.update {
                it.copy(
                    password = event.password,
                    passwordError = null
                )
            }

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
//                email != mockEmail -> AuthError.EmailNotFound
                else -> null
            }

            val passwordErr = when {
                password.isBlank() -> AuthError.PasswordBlank
//                email == mockEmail && password != mockPassword -> AuthError.PasswordInvalid
                else -> null
            }

            val hasErrors = emailErr != null || passwordErr != null

            if (hasErrors) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        emailError = emailErr,
                        passwordError = passwordErr,
                        isUserLoggedIn = false
                    )
                }
            } else {
                val response = authRepository.login(email, password)

                response
                    .onSuccess { token ->
                        println("token = $token")
                        _state.update {
                            it.copy(
                                isLoading = false,
                                emailError = null,
                                passwordError = null,
                                isUserLoggedIn = true
                            )
                        }
                        _effect.send(LoginEffect.LoginSuccess)
                    }
                    .onFailure { e ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                emailError = null,
                                passwordError = AuthError.EmailNotFound,
                            )
                        }

                    }
            }
        }
    }
}
