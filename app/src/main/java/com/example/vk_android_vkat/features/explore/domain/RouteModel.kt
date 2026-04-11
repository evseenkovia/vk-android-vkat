package com.example.vk_android_vkat.features.explore.domain

data class RouteModel(
    val id: Int,
    val title: String,
    val description: String,
    val distanceKm: Int,
    val durationHours: Int,
    val pointsCount: Int,   // заглушка для списка мест
    val rating: Float,      // заглушка для рейтинга
    val imageUrl: String? = null,
    val authorID: String,
    var isFavourite: Boolean = false
)