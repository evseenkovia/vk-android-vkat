package com.example.vk_android_vkat.features.explore.data.local

import com.example.vk_android_vkat.features.explore.domain.RouteModel

interface LocalDataSource {
    suspend fun getFavouriteRoutes(): List<RouteModel>
    suspend fun getFavouriteById(routeId: Long): RouteModel?
    suspend fun addFavourite(route: RouteModel)
    suspend fun removeFavourite(routeId: Long)
}