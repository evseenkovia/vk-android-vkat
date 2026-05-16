package com.example.vk_android_vkat.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.data.delayTime
import com.example.vk_android_vkat.data.mockEmail
import com.example.vk_android_vkat.data.mockPassword
import com.example.vk_android_vkat.features.auth.AuthError
import com.vk.id.AccessToken
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

    // функция определяет
    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.EmailChanged -> _state.update { it.copy(email = event.email, emailError = null) }
            is LoginEvent.PasswordChanged -> _state.update { it.copy(password = event.password, passwordError = null) }
            LoginEvent.LoginClicked -> login()
            LoginEvent.RegisterClicked -> viewModelScope.launch { _effect.send(LoginEffect.GoToRegistration) }
            LoginEvent.ForgotPasswordClicked -> viewModelScope.launch { _effect.send(LoginEffect.GoToPasswordRecovery) }
            is LoginEvent.VKAuthSuccess -> viewModelScope.launch { handleVKAuthSuccess(event.accessToken) }
            is LoginEvent.VKAuthError -> handleVKAuthError(event.error)
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

    // VK Авторизация
//    private fun startVKAuth() {
//        _state.update { it.copy(isVKAuthInProgress = true, vkAuthError = null) }
//
//        viewModelScope.launch {
//            try {
//                // Создаем колбэк
//                val callback = object : VKIDAuthCallback {
//                    override fun onAuth(accessToken: AccessToken) {
//                        // Успешная авторизация
//                        viewModelScope.launch {
//                            handleVKAuthSuccess(accessToken)
//                        }
//                    }
//
//                    override fun onFail(fail: VKIDAuthFail) {
//                        // Ошибка авторизации
//                        viewModelScope.launch {
//                            handleVKAuthError(fail.description)
//                        }
//                    }
//                }
//
//                // Запускаем авторизацию с параметрами
//                VKID.instance.authorize(
//                    callback = callback,
//                    params = VKIDAuthParams {
//                        scopes = setOf("wall", "photos", "friends")
//                    }
//                )
//            } catch (e: Exception) {
//                handleVKAuthError(e.message ?: "Unknown error")
//            }
//        }
//    }

    private suspend fun handleVKAuthSuccess(accessToken: AccessToken) {
        // Сохраняем токен
//        tokenManager.saveToken(accessToken)

        _state.update {
            it.copy(
                isVKAuthInProgress = false,
                isUserLoggedIn = true
            )
        }

        // Отправляем эффект навигации
        _effect.send(LoginEffect.LoginSuccess)
    }

    private fun handleVKAuthError(error: String) {
        _state.update {
            it.copy(
                isVKAuthInProgress = false,
                vkAuthError = error
            )
        }
    }
}
