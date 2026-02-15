package com.example.vk_android_vkat.ui.profile

data class ProfileContentUi(
    val header: ProfileHeaderUi,
    val sections: List<ProfileItemUi>
)

data class ProfileHeaderUi(
    val avatarUrl: String,
    val userName: String,
    val email: String
)
