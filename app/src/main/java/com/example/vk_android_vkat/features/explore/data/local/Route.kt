package com.example.vk_android_vkat.features.explore.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vk_android_vkat.features.editor.domain.RoutePointModel

@Entity(tableName = "Route")
data class Route(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val distanceKm: Int,
    val durationHours: Int,
    val pointsCount: Int,
    val rating: Float,
    val imageUrl: String?,
    val isFavourite: Boolean,
    val tags: List<String> = emptyList(),          // <-- добавить
    val points: List<RoutePointModel> = emptyList() // <-- добавить
)