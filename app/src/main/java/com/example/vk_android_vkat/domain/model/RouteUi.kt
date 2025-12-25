package com.example.vk_android_vkat.domain.model

data class RoutePoint(
    val name: String,
    val address: String,
    val description: String
)

data class RouteUi(
    val id: Long,
    val title: String,
    val description: String,
    val distanceKm: Int,
    val durationHours: Int,
    val pointsCount: Int,
    val rating: Float,
    val imageUrl: String? = null,
    val isFavourite: Boolean = false,
    val points: List<RoutePoint> = emptyList()
)
