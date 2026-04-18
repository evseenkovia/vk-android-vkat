package com.example.vk_android_vkat.features.explore.domain

import com.example.vk_android_vkat.features.explore.domain.filter.RouteFilter
import kotlinx.coroutines.flow.StateFlow

interface RouteRepository {
    val routesFlow: StateFlow<List<RouteModel>>

    suspend fun getAllRoutes() : Result<List<RouteModel>>

    suspend fun getRouteById(id: Int): Result<RouteModel>

    suspend fun getRouteByFilter(filter: RouteFilter): Result<List<RouteModel>>

    suspend fun findRouteByQuery(query: String?): Result<List<RouteModel>>

    suspend fun addRouteToFavourites(vararg routeModels: RouteModel)

    suspend fun getAllFavourites() : Result<List<RouteModel>>

    suspend fun deleteFromFavourites(id: Int)
    suspend fun addRoute(route: RouteModel)
    fun getNextId(): Int
}