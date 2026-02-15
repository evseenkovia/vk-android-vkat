package com.example.vk_android_vkat.ui.profile

sealed interface ProfileItemUi {
    val title: String

    data class Info(override val title: String, val subtitle: String? = null) : ProfileItemUi
    data class Switch(override val title: String, val checked: Boolean) : ProfileItemUi
    data class Navigation(override val title: String, val section: ProfileSection) : ProfileItemUi
}

sealed class ProfileSection(val title: String) {
    object Account : ProfileSection("Аккаунт")
    object Notifications : ProfileSection("Уведомления")
    object Privacy : ProfileSection("Конфиденциальность")
    object Appearance : ProfileSection("Оформление")
    object About : ProfileSection("О приложении")
}
