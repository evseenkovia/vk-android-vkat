package com.example.vk_android_vkat.features.editor.map

import com.yandex.mapkit.geometry.Point
import kotlinx.serialization.Serializable

@Serializable
data class AddressPoint(
    val address: String,
    val latitude: Double,
    val longitude: Double
)

data class MapState(
    val cameraPosition: Point = Point(55.753089, 37.622651), // Москва по умолчанию
    val zoom: Float = 10f,
    val lastPlacemark: Point? = null,
    val placemarks: List<Point> = emptyList(),
    val initialLocationSet: Boolean = false
)
sealed interface MapEvent {
    data class LongTap(val cords: Point) : MapEvent
    data class CameraSave(val point: Point, val zoom: Float) : MapEvent
    data class ConfirmPlacemark(val currentZoom: Int) : MapEvent
    data class OnCurrentLocation(val point: Point) : MapEvent
}
sealed interface MapEffect {
    data class Chosen(val point: Point, val address: String) : MapEffect
}