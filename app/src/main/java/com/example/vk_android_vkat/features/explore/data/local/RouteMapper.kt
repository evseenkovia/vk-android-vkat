package com.example.vk_android_vkat.features.explore.data.local

import com.example.vk_android_vkat.features.explore.domain.RouteModel

fun Route.toRouteModel(): RouteModel = RouteModel(
    id = id,
    title = title,
    description = description,
    distanceKm = distanceKm,
    durationHours = durationHours,
    pointsCount = pointsCount,
    rating = rating,
    imageUrl = imageUrl ?: "",
    tags = tags,
    points = points,
    isFavourite = isFavourite,
    authorID = authorID
)

fun RouteModel.toRoute(): Route = Route(
    id = id,
    title = title,
    description = description,
    distanceKm = distanceKm,
    durationHours = durationHours,
    pointsCount = pointsCount,
    rating = rating,
    imageUrl = imageUrl,
    isFavourite = isFavourite,
    tags = tags,
    points = points,
    authorID = authorID
)