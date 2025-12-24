package com.example.vk_android_vkat.ui.profile

import androidx.lifecycle.ViewModel
import com.example.vk_android_vkat.mock_data.mockProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        // Пока мок-данные
        _state.value = mockProfile
    }

    fun toggleNotifications(enabled: Boolean) {
        _state.update { it.copy(notificationsEnabled = enabled) }
    }

    fun toggleDarkTheme(enabled: Boolean) {
        _state.update { it.copy(darkThemeEnabled = enabled) }
    }

}