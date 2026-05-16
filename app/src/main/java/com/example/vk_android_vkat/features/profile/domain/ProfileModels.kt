package com.example.vk_android_vkat.features.profile.domain

sealed interface ProfileUiState {
    object Loading : ProfileUiState
    data class Error(val message: String) : ProfileUiState
    data class Content(val data: ProfileContentUi) : ProfileUiState
}

data class ProfileContentUi(
    val header: ProfileHeaderUi,
    val sections: List<ProfileItemUi>
)

data class ProfileHeaderUi(
    val userName: String,
    val email: String,
    val avatarUrl: String? = null
)

// UI события
sealed interface ProfileUiEvent {
    data class ItemClicked(val id: ProfileItemUi) : ProfileUiEvent
    data class SwitchChanged(val id: ProfileItemUi, val checked: Boolean) : ProfileUiEvent
    object Retry : ProfileUiEvent
}

// UI модели для элементов списка
sealed class ProfileItemUi(
    open val title: String,
    open val id: String = title
) {
    data class Info(
        override val title: String,
        val subtitle: String? = null,
        override val id: String = title
    ) : ProfileItemUi(title, id)

    data class Switch(
        override val title: String,
        val checked: Boolean = false,
        override val id: String = title
    ) : ProfileItemUi(title, id)

    data class Navigation(
        override val title: String,
        override val id: String = title
    ) : ProfileItemUi(title, id)
}
