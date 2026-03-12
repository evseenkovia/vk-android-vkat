package com.example.vk_android_vkat.features.explore.routeinfo.ui

data class TopBarUiModel(
    val title: String = "",
    val isFavourite: Boolean = false,
    val onBack: (() -> Unit)? = null,
    val onFavouriteToggle: (() -> Unit)? = null
)