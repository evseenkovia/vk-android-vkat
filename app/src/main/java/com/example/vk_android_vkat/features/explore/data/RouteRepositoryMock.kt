package com.example.vk_android_vkat.features.explore.data

import com.example.vk_android_vkat.data.mockRoutes
import com.example.vk_android_vkat.features.explore.routeinfo.domain.RouteRepository

class RouteRepositoryMock : RouteRepository {
    private val routes = mockRoutes
    override suspend fun getRouteById(id: Long) = routes.find { it.id == id }
}