package com.example.vk_android_vkat.features.map

import com.example.vk_android_vkat.features.editor.domain.RoutePointModel

data class MapState(
    val points: List<RoutePointModel> = emptyList(),
)

sealed interface MapEvent{

}