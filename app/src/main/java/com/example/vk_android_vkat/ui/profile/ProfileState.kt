package com.example.vk_android_vkat.ui.profile

data class ProfileState(
    // Основная информация
    val name: String = "",
    val email: String = "",
    val avatarUrl: String? = null,

    // Настройки
    val notificationsEnabled: Boolean = true,
    val darkThemeEnabled: Boolean = false,

    // Служебное (на будущее)
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
