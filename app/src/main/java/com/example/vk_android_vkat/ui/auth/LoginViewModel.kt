package com.example.vk_android_vkat.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        // В реальности может быть проверка токена
        delay(500) // Имитация задержки
        return true // По умолчанию не залогинен -> false
    }

    fun setMode(mode: AuthMode) {
        _state.update { it.copy(mode = mode) }
    }

    fun updateEmail(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun login() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                delay(1000) // Имитация API запроса

                // TODO: Реальная логика авторизации
                // Если успешно:
                saveAuthToken("fake_token_123")

                _state.update {
                    it.copy(
                        isLoading = false,
                        isUserLoggedIn = true // Устанавливаем флаг успешного входа
                    )
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Ошибка входа: ${e.message}"
                    )
                }
            }
        }
    }

    private fun saveAuthToken(token: String) {
        // Сохраняем токен в SharedPreferences
        // Это упростит проверку при следующем запуске
        // TODO: использовать EncryptedSharedPreferences для безопасности
    }

    fun register() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            delay(1000) // Имитация запроса
            // TODO: Реальная регистрация
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _state.update { it.copy(confirmPassword = confirmPassword, error = null) }
    }
}