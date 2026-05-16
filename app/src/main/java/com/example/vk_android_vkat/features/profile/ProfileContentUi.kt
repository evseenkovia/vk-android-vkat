package com.example.vk_android_vkat.features.profile

data class ProfileContentUi(
    val header: ProfileHeaderUi,
    val sections: List<ProfileItemUi>
)

data class ProfileHeaderUi(
    val avatarUrl: String? = null,
    val userName: String,
    val email: String
)
