package com.example.vk_android_vkat.features.explore.data.local

import com.example.vk_android_vkat.features.explore.domain.RouteModel

fun Route.toRouteModel() : RouteModel {
    return RouteModel(
        id = this.id,
        title = this.title,
        description = this.title,
        distanceKm = this.distanceKm,
        durationHours = this.durationHours,
        pointsCount = this.pointsCount,
        rating = this.rating,
        imageUrl = this.imageUrl,
        isFavourite = this.isFavourite
    )
}

fun RouteModel.toRoute() : Route {
    return Route(
        id = this.id,
        title = this.title,
        description = this.title,
        distanceKm = this.distanceKm,
        durationHours = this.durationHours,
        pointsCount = this.pointsCount,
        rating = this.rating,
        imageUrl = this.imageUrl,
        isFavourite = this.isFavourite
    )
}