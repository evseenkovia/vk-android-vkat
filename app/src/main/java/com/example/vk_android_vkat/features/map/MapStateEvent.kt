package com.example.vk_android_vkat.features.map


data class MapState(
    val markers: List<RouteStartPoint> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class RouteStartPoint(
    val id: Int,
    val title: String,
    val lat: Double,
    val lng: Double
)