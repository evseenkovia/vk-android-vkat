package com.example.vk_android_vkat.features.map

import com.yandex.mapkit.geometry.Point

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
data class CameraState(
    val latitude: Double = 55.753089,
    val longitude: Double = 37.622651,
    val zoom: Float = 10f,
    val azimuth: Float = 0f,
    val tilt: Float = 0f
) {
    val target: Point
        get() = Point(latitude, longitude)
}

class LocationPermissionState(
    val hasPermission: Boolean,
    val requestPermission: () -> Unit
)