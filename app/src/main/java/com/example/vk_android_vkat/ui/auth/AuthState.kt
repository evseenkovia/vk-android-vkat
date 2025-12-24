package com.example.vk_android_vkat.ui.auth

sealed class AuthMode {
    object Login : AuthMode()
    object Register : AuthMode()
    object ForgotPassword : AuthMode()
}

data class AuthState(
    val mode: AuthMode = AuthMode.Login,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = true, // true по умолчанию для сплеша
    val isAuthChecked: Boolean = false, // Проверка авторизации завершена?
    val isUserLoggedIn: Boolean = false, // Результат проверки

    //Обработка ошибок
    val error: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
)