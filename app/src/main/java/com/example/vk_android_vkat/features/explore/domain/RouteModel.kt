package com.example.vk_android_vkat.features.explore.domain

import com.example.vk_android_vkat.features.explore.data.remote.RouteDto

data class RouteModel(
    val id: Int,
    val title: String,
    val description: String,
    val distanceKm: Int,
    val durationHours: Int,
    val pointsCount: Int,   // заглушка для списка мест
    val rating: Float,      // заглушка для рейтинга
    val imageUrl: String? = null,
    var isFavourite: Boolean = false
)

fun RouteModel.toRouteDto(): RouteDto {
    return RouteDto(
        id = this.id,
        title = this.title,
        description = this.description,
        distanceKm = this.distanceKm,
        durationHrs = this.durationHours,
        points = this.pointsCount,
        rating = this.rating.toDouble(),  // Float -> Double
        imageUrl = this.imageUrl
    )
}

fun List<RouteModel>.toRouteDtoList(): List<RouteDto> {
    return this.map { it.toRouteDto() }
}
