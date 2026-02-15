package com.example.vk_android_vkat.ui.auth

data class AuthState(
    val mode: AuthMode = AuthMode.Login,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = true, // true по умолчанию для сплеша
    val isAuthChecked: Boolean = false, // Проверка авторизации завершена?
    val isUserLoggedIn: Boolean = false, // Результат проверки

    // Обработка ошибок
    val error: Int? = null,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val confirmPasswordError: Int? = null,

    // Переходы на другие экраны
    val navEvent: AuthNavEvent? = null
)
sealed class AuthEvent {
    // Ввод текста
    data class EmailChanged(val email: String) : AuthEvent()
    data class PasswordChanged(val password: String) : AuthEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : AuthEvent()

    // Смена режима
    data class ModeChanged(val mode: AuthMode) : AuthEvent()

    // Действия пользователя
    object LoginClicked : AuthEvent()
    object RegisterClicked : AuthEvent()
    object ForgotPasswordClicked : AuthEvent()

    // Результаты асинхронных операций
    data class AuthSuccess(val isLoggedIn: Boolean) : AuthEvent()
    data class AuthError(val error: Int) : AuthEvent()

    // Сброс ошибок
    object ClearErrors : AuthEvent()
}

sealed class AuthMode {
    object Login : AuthMode()
    object Registration : AuthMode()
    object PasswordRecovery : AuthMode()
}

sealed class AuthNavEvent {
    object ToMain : AuthNavEvent()
    object ToLogin : AuthNavEvent()
    object ToRegister : AuthNavEvent()
    object ToPasswordRecovery : AuthNavEvent()
}