package com.example.vk_android_vkat.features.auth.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.data.delayTime
import com.example.vk_android_vkat.features.auth.AuthError
import com.example.vk_android_vkat.features.auth.data.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel(
    val repository: AuthRepository
) : ViewModel() {

    sealed class RegistrationEffect {
        object RegistrationSuccess : RegistrationEffect()
        object GoToLogin : RegistrationEffect()
    }

    private val _effect = Channel<RegistrationEffect>()
    val effect = _effect.receiveAsFlow()

    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.NameChanged -> _state.update {
                it.copy(
                    name = event.name,
                    nameError = null
                )
            }

            is RegistrationEvent.EmailChanged -> _state.update {
                it.copy(
                    email = event.email,
                    emailError = null
                )
            }

            is RegistrationEvent.PasswordChanged -> _state.update {
                it.copy(
                    password = event.password,
                    passwordError = null
                )
            }

            is RegistrationEvent.ConfirmPasswordChanged -> _state.update {
                it.copy(
                    confirmPassword = event.confirm,
                    confirmPasswordError = null
                )
            }

            RegistrationEvent.ClearErrors -> _state.update {
                it.copy(
                    emailError = null,
                    passwordError = null,
                    confirmPasswordError = null
                )
            }

            RegistrationEvent.RegisterClicked -> register()
            RegistrationEvent.LoginClicked -> viewModelScope.launch {
                _effect.send(
                    RegistrationEffect.GoToLogin
                )
            }
        }
    }

    private fun register() {
        _state.update { it.copy(isLoading = true, emailError = null, passwordError = null) }

        viewModelScope.launch {
            delay(delayTime)

            val email = state.value.email
            val password = state.value.password
            val confirm = state.value.confirmPassword

            val emailErr = when {
                email.isBlank() -> AuthError.EmailBlank
                !email.contains("@") -> AuthError.EmailInvalid
                else -> null
            }

            val passwordErr = when {
                password.length < 6 -> AuthError.PasswordTooShort
                else -> null
            }

            val confirmErr = when {
                password != confirm -> AuthError.PasswordsMismatch
                else -> null
            }

            val hasErrors = emailErr != null || passwordErr != null || confirmErr != null

            if (hasErrors) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        emailError = emailErr,
                        passwordError = passwordErr,
                        confirmPasswordError = confirmErr,
                        isRegistered = false
                    )
                }
            } else {
                val response = repository.signUp(email, password)

                response
                    .onSuccess { token ->
                        println("token = $token")
                        // todo -> что делать с токеном, если мы редиректим на логин???
                        _state.update {
                            it.copy(
                                isLoading = false,
                                emailError = null,
                                passwordError = null,
                            )
                        }
                        _effect.send(RegistrationEffect.RegistrationSuccess)
                    }
                    .onFailure { e ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                emailError = null,
                                passwordError = null,
                                // todo -> вывести сообщение об ошибке
                            )
                        }
                    }
            }
        }
    }
}
