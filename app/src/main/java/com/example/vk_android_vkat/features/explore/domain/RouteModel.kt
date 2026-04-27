package com.example.vk_android_vkat.features.explore.domain

import com.example.vk_android_vkat.features.editor.domain.RoutePointModel

data class RouteModel(
    val id: Int,
    val title: String,
    val description: String,
    val distanceKm: Int,
    val durationHours: Int,
    val pointsCount: Int,   // заглушка для списка мест
    val rating: Float,      // заглушка для рейтинга
    val imageUrl: String? = null,
    val tags: List<String> = emptyList(),
    val points: List<RoutePointModel> = emptyList(),
    var isFavourite: Boolean = false,
    val authorID: String? = null
)