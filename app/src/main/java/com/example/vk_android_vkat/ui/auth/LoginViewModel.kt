package com.example.vk_android_vkat.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.R
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
        // Проверяем, залогинен ли пользователь при старте
        checkInitialAuth()
    }

    private fun checkInitialAuth() {
        viewModelScope.launch {
            // TODO Сделать простую проверку через SharedPreferences
            val isLoggedIn = checkIfUserLoggedIn()
            _state.update {
                it.copy(
                    isLoading = false,
                    isAuthChecked = true,
                    isUserLoggedIn = isLoggedIn
                )
            }
        }
    }

    private suspend fun checkIfUserLoggedIn(): Boolean {
        delay(delayTime)
        return true // По умолчанию пользователь не залогинен
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

    // Обновление email
    fun updateEmail(email: String) {
        _state.update { it.copy(email = email) }
    }

    // Обновление пароля
    fun updatePassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    // Обновление поля подтверждения пароля
    fun updateConfirmPassword(confirmPassword: String) {
        _state.update { it.copy(confirmPassword = confirmPassword, error = null) }
    }

    // Сохраняем токен в SharedPreferences
    private fun saveAuthToken(token: String) {
        // TODO Реализовать сохранение токена
    }

    fun login(context: Context) {
        // Сбрасываем предыдущие ошибки
        _state.update { it.copy(isLoading = true, error = null, emailError = null, passwordError = null) }

        viewModelScope.launch {
            delay(delayTime) // Имитация API запроса

            val email = state.value.email
            val password = state.value.password

            // Проверяем email
            val emailErr = when {
                email.isBlank() -> context.getString(R.string.email_blank_error) // Поле пустое
                !email.contains("@") -> context.getString(R.string.email_invalid_error) // Некорректный формат
                email != mockEmail -> context.getString(R.string.email_not_found_error) // Email не найден
                else -> null
            }

            // Проверяем пароль
            val passwordErr = when {
                password.isBlank() -> context.getString(R.string.password_blank_error) // Пароль пустой
                email == mockEmail && password != mockPassword -> context.getString(R.string.password_invalid_error) // Неверный пароль
                else -> null
            }

            val hasErrors = emailErr != null || passwordErr != null

            // Обновляем состояние с результатом проверки
            _state.update {
                it.copy(
                    isLoading = false,
                    emailError = emailErr,
                    passwordError = passwordErr,
                    isUserLoggedIn = !hasErrors
                )
            }
        }
    }

    fun register(context: Context) {
        _state.update { it.copy(isLoading = true, emailError = null, passwordError = null, confirmPasswordError = null) }

        viewModelScope.launch {
            delay(delayTime) // Имитация API запроса

            val email = state.value.email
            val password = state.value.password
            val confirm = state.value.confirmPassword

            // Проверки email
            val emailErr = when {
                email.isBlank() -> context.getString(R.string.email_blank_error) // Поле пустое
                !email.contains("@") -> context.getString(R.string.email_invalid_error) // Некорректный формат
                else -> null
            }

            // Проверки пароля
            val passwordErr = when {
                password.length < 6 -> context.getString(R.string.password_short_error) // Слишком короткий
                else -> null
            }

            // Проверка подтверждения пароля
            val confirmErr = when {
                password != confirm -> context.getString(R.string.passwords_mismatch_error) // Не совпадает с паролем
                else -> null
            }

            val hasErrors = emailErr != null || passwordErr != null || confirmErr != null

            // Обновляем состояние с результатами проверки
            _state.update {
                it.copy(
                    isLoading = false,
                    emailError = emailErr,
                    passwordError = passwordErr,
                    confirmPasswordError = confirmErr,
                    error = if (!hasErrors) context.getString(R.string.register_success) else null // Успешная регистрация
                )
            }
        }
    }

    fun sendPasswordReset(context: Context) {
        _state.update { it.copy(isLoading = true, emailError = null, error = null) }

        viewModelScope.launch {
            delay(2000) // Имитация API запроса

            val email = state.value.email

            // Проверка email для восстановления пароля
            val newState = when {
                email.isBlank() -> state.value.copy(
                    isLoading = false,
                    emailError = context.getString(R.string.email_blank_error) // Поле пустое
                )
                email != mockEmail -> state.value.copy(
                    isLoading = false,
                    emailError = context.getString(R.string.email_not_found_error) // Email не найден
                )
                else -> state.value.copy(
                    isLoading = false,
                    error = context.getString(R.string.password_reset_sent) // Ссылка отправлена
                )
            }

            _state.value = newState
        }
    }

}
