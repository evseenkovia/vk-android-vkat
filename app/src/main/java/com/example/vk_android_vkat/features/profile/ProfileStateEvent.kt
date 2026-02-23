package com.example.vk_android_vkat.features.profile

sealed interface ProfileUiState {
    object Loading : ProfileUiState
    data class Error(val message: String) : ProfileUiState
    class Content(val data : ProfileContentUi) : ProfileUiState
}

// UI события
sealed interface ProfileUiEvent {
    data class ItemClicked(val id: ProfileItemUi) : ProfileUiEvent
    data class SwitchChanged(val id: ProfileItemUi, val checked: Boolean) : ProfileUiEvent
    object Retry : ProfileUiEvent
}
