package com.example.vk_android_vkat.features.explore.data.remote

data class RouteDto(
    val id: Int,
    val title: String,
    val description: String,
    val distanceKm: Int,
    val durationHrs: Int,
    val points: Int,
    val rating: Double,
    val imageUrl: String? = null
)
