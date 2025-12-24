package com.example.vk_android_vkat.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.mock_data.delayTime
import com.example.vk_android_vkat.mock_data.mockEmail
import com.example.vk_android_vkat.mock_data.mockPassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    init {
        checkInitialAuth()
    }

    private fun checkInitialAuth() {
        viewModelScope.launch {
            // Проверяем, залогинен ли пользователь
            val isLoggedIn = checkIfUserLoggedIn()

            _state.update {
                it.copy(
                    isLoading = false,
                    // Добавляем флаг, что проверка завершена
                    isAuthChecked = true,
                    isUserLoggedIn = isLoggedIn
                )
            }
        }
    }

    private suspend fun checkIfUserLoggedIn(): Boolean {
        //TODO Сделать простую проверку через SharedPreferences
        delay(delayTime)
        return false // По умолчанию не залогинен
    }

    fun setMode(mode: AuthMode) {
        _state.update {
            it.copy(
                mode = mode,
                email = "",
                password = "",
                confirmPassword = "",
                error = null,
                emailError = null,
                passwordError = null,
                confirmPasswordError = null,
                isLoading = false
            )
        }
    }

    fun updateEmail(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _state.update { it.copy(confirmPassword = confirmPassword, error = null) }
    }

    // Сохраняем токен в SharedPreferences
    private fun saveAuthToken(token: String) {}

    fun login() {
        _state.update { it.copy(isLoading = true, error = null, emailError = null, passwordError = null) }

        viewModelScope.launch {
            delay(delayTime)

            val email = state.value.email
            val password = state.value.password

            // Собираем все ошибки сразу
            val emailErr = when {
                email.isBlank() -> "Email не может быть пустым"
                !email.contains("@") -> "Некорректный email"
                email != mockEmail -> "Пользователь с таким email не найден"
                else -> null
            }

            val passwordErr = when {
                password.isBlank() -> "Пароль не может быть пустым"
                email == mockEmail && password != mockPassword -> "Неверный пароль"
                else -> null
            }

            val hasErrors = emailErr != null || passwordErr != null

            _state.update {
                it.copy(
                    isLoading = false,
                    emailError = emailErr,
                    passwordError = passwordErr,
                    isUserLoggedIn = if (!hasErrors) true else false
                )
            }
        }
    }

    fun register() {
        _state.update { it.copy(isLoading = true, emailError = null, passwordError = null, confirmPasswordError = null) }

        viewModelScope.launch {
            delay(delayTime)

            val email = state.value.email
            val password = state.value.password
            val confirm = state.value.confirmPassword

            val emailErr = when {
                email.isBlank() -> "Email не может быть пустым"
                !email.contains("@") -> "Некорректный email"
                else -> null
            }

            val passwordErr = when {
                password.length < 6 -> "Пароль слишком короткий"
                else -> null
            }

            val confirmErr = when {
                password != confirm -> "Пароли не совпадают"
                else -> null
            }

            val hasErrors = emailErr != null || passwordErr != null || confirmErr != null

            _state.update {
                it.copy(
                    isLoading = false,
                    emailError = emailErr,
                    passwordError = passwordErr,
                    confirmPasswordError = confirmErr,
                    error = if (!hasErrors) "Регистрация успешна" else null
                )
            }
        }
    }

    fun sendPasswordReset() {
        _state.update { it.copy(isLoading = true, emailError = null, error = null) }

        viewModelScope.launch {
            delay(2000) // имитация API

            val email = state.value.email

            val newState = when {
                email.isBlank() -> state.value.copy(
                    isLoading = false,
                    emailError = "Email не может быть пустым"
                )
                email != mockEmail -> state.value.copy(
                    isLoading = false,
                    emailError = "Пользователь с таким email не найден"
                )
                else -> state.value.copy(
                    isLoading = false,
                    error = "Ссылка на восстановление отправлена"
                )
            }

            _state.value = newState
        }
    }

}