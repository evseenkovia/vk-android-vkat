package com.example.vk_android_vkat.features.explore.data.remote

import com.example.vk_android_vkat.features.explore.domain.RouteModel

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

fun RouteDto.toRouteModel(): RouteModel {
    return RouteModel(
        id = this.id,
        title = this.title,
        description = this.description,
        distanceKm = this.distanceKm,
        durationHours = this.durationHrs,
        pointsCount = this.points,
        rating = this.rating.toFloat(),  // Double -> Float
        imageUrl = this.imageUrl,
        isFavourite = false
    )
}

fun List<RouteDto>.toRouteModelList(): List<RouteModel> {
    return this.map { it.toRouteModel() }
}
