package com.example.vk_android_vkat.features.explore.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Route(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val distanceKm: Int,
    val durationHours: Int,
    val pointsCount: Int,   // заглушка для списка мест (возможно новая Entity - переделать под API)
    val rating: Float,      // расчет из разных отзывов и тд.
    val imageUrl: String? = null,
    val authorID: String,
    val isFavourite: Boolean
)
