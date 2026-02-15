package com.example.vk_android_vkat.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.data.delayTime
import com.example.vk_android_vkat.data.mockEmail
import com.example.vk_android_vkat.data.mockPassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

//    init {
//        // Проверяем, залогинен ли пользователь при старте
//        checkInitialAuth()
//    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged -> updateEmail(event.email)
            is AuthEvent.PasswordChanged -> updatePassword(event.password)
            is AuthEvent.ConfirmPasswordChanged -> updateConfirmPassword(event.confirmPassword)
            is AuthEvent.ModeChanged -> setMode(event.mode)
            AuthEvent.LoginClicked -> login()
            AuthEvent.RegisterClicked -> register()
            AuthEvent.ForgotPasswordClicked -> sendPasswordReset()
            is AuthEvent.AuthSuccess -> _state.update { it.copy(isUserLoggedIn = event.isLoggedIn) }
            is AuthEvent.AuthError -> _state.update { it.copy(error = event.error) }
            AuthEvent.ClearErrors -> clearErrors()
        }
    }

    private suspend fun checkIfUserLoggedIn(): Boolean {
        delay(delayTime)
        return true // По умолчанию пользователь не залогинен
    }

    private fun setMode(mode: AuthMode) {
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
    private fun updateEmail(email: String) {
        _state.update { it.copy(email = email, emailError = null, error = null) }
    }

    // Обновление пароля
    private fun updatePassword(password: String) {
        _state.update { it.copy(password = password, passwordError = null, error = null) }
    }

    // Обновление поля подтверждения пароля
    private fun updateConfirmPassword(confirm: String) {
        _state.update { it.copy(confirmPassword = confirm, confirmPasswordError = null, error = null) }
    }

    private fun login() {
        // Сбрасываем предыдущие ошибки
        _state.update { it.copy(isLoading = true, error = null, emailError = null, passwordError = null) }

        viewModelScope.launch {
            delay(delayTime) // Имитация API запроса

            val email = state.value.email
            val password = state.value.password

            // Проверяем email
            val emailErr = when {
                email.isBlank() -> R.string.email_blank_error // Поле пустое
                !email.contains("@") -> R.string.email_invalid_error // Некорректный формат
                email != mockEmail -> R.string.email_not_found_error // Email не найден
                else -> null
            }

            // Проверяем пароль
            val passwordErr = when {
                password.isBlank() -> R.string.password_blank_error // Пароль пустой
                email == mockEmail && password != mockPassword -> R.string.password_invalid_error // Неверный пароль
                else -> null
            }

            val hasErrors = emailErr != null || passwordErr != null

            // Обновляем состояние с результатом проверки
            _state.update {
                it.copy(
                    isLoading = false,
                    emailError = emailErr,
                    passwordError = passwordErr,
                    isUserLoggedIn = !hasErrors,
                    navEvent = if (!hasErrors) AuthNavEvent.ToMain else null
                )
            }
        }
    }

    private fun register() {
        _state.update { it.copy(isLoading = true, emailError = null, passwordError = null, confirmPasswordError = null) }

        viewModelScope.launch {
            delay(delayTime) // Имитация API запроса

            val email = state.value.email
            val password = state.value.password
            val confirm = state.value.confirmPassword

            // Проверки email
            val emailErr = when {
                email.isBlank() -> R.string.email_blank_error // Поле пустое
                !email.contains("@") -> R.string.email_invalid_error // Некорректный формат
                else -> null
            }

            // Проверки пароля
            val passwordErr = when {
                password.length < 6 -> R.string.password_short_error // Слишком короткий
                else -> null
            }

            // Проверка подтверждения пароля
            val confirmErr = when {
                password != confirm -> R.string.passwords_mismatch_error // Не совпадает с паролем
                else -> null
            }

            val hasErrors = emailErr != null || passwordErr != null || confirmErr != null

            if (!hasErrors) {
//                saveUserData(context, email, password)
                mockSaveUser(email, password)
                // обновляем состояние
                _state.update {
                    it.copy(
                        isLoading = false,
                        isUserLoggedIn = true,
                        error = null
                    )
                }
            } else {
                // Обновляем состояние с результатами проверки
                _state.update {
                    it.copy(
                        isLoading = false,
                        emailError = emailErr,
                        passwordError = passwordErr,
                        confirmPasswordError = confirmErr,
                        error = R.string.unknown_error
                    )
                }
            }
        }
    }

    private fun saveUserData(context: Context, email: String, password: String) {
        // TODO: Реальная логика сохранения пользователя
        // Пока просто моковая реализация
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("email", email)
            .putString("password", password)
            .putBoolean("is_logged_in", true)
            .apply()
    }

    private fun sendPasswordReset() {
        _state.update { it.copy(isLoading = true, emailError = null, error = null) }

        viewModelScope.launch {
            delay(2000) // Имитация API запроса

            val email = state.value.email

            // Сообщение об ошибке
            val errorMessage = when {
                email.isBlank() -> R.string.email_blank_error
                email != mockEmail -> R.string.email_not_found_error
                else -> R.string.password_reset_sent
            }

            _state.update { it.copy(isLoading = false, error = errorMessage) }
        }
    }

    private fun clearErrors() {
        _state.update {
            it.copy(
                error = null,
                emailError = null,
                passwordError = null,
                confirmPasswordError = null
            )
        }
    }

    private fun mockSaveUser(email: String, password: String) {
        // Заглушка без Context
    }
}
