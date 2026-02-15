package com.example.vk_android_vkat.ui.profile

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Error(val message: String) : ProfileUiState
    data class Content(val data : ProfileContentUi) : ProfileUiState
}

// UI события
sealed interface ProfileUiEvent {
    data class ItemClicked(val id: ProfileItemUi) : ProfileUiEvent
    data class SwitchChanged(val id: ProfileItemUi, val checked: Boolean) : ProfileUiEvent
    data object Retry : ProfileUiEvent
}
