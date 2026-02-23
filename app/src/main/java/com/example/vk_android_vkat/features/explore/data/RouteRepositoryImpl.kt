package com.example.vk_android_vkat.features.explore.data

import com.example.vk_android_vkat.domain.model.RouteModel
import com.example.vk_android_vkat.features.explore.routeinfo.domain.RouteRepository

class RouteRepositoryImpl() : RouteRepository {
    override suspend fun getRouteById(id: Long): RouteModel? {
        TODO("Not yet implemented")
    }
}