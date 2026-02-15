package com.example.vk_android_vkat.domain.model

data class RouteModel(
    val id: Long,
    val title: String,
    val description: String,
    val distanceKm: Int,
    val durationHours: Int,
    val pointsCount: Int,   // заглушка для списка мест
    val rating: Float,      // заглушка для рейтинга
    val imageUrl: String? = null,
    val isFavourite: Boolean = false
)