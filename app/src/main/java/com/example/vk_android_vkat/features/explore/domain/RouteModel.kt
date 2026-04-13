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
    val points: List<RoutePointModel> = emptyList(),
    val imageUrl: String? = null,
    var isFavourite: Boolean = false
)