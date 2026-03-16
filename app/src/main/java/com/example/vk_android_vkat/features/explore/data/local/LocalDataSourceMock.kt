package com.example.vk_android_vkat.features.explore.data.local

import com.example.vk_android_vkat.features.explore.domain.RouteModel

class LocalDataSourceMock : LocalDataSource {

    // внутренний список избранных маршрутов
    private val favourites = mutableListOf<RouteModel>()


    override suspend fun getFavouriteRoutes(): List<RouteModel> {
        return favourites.toList()
    }

    override suspend fun getFavouriteById(routeId: Long): RouteModel? {
        return favourites.find { it.id == routeId }
    }

    override suspend fun addFavourite(route: RouteModel) {
        if (favourites.none { it.id == route.id }) {
            favourites.add(route.copy(isFavourite = true))
        }
    }
    override suspend fun removeFavourite(routeId: Long) {
        favourites.removeIf { it.id == routeId }
    }

}