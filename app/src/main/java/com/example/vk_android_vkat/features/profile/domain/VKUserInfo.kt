package com.example.vk_android_vkat.features.profile.domain

data class VKUserInfo(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val avatarUrl: String?,
    val email: String?,
    val phone: String?
) {
    val fullName: String get() = "$firstName $lastName"
}
