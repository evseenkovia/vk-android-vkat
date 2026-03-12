package com.example.vk_android_vkat.features.explore.data

import com.example.vk_android_vkat.features.explore.domain.RouteModel
import com.example.vk_android_vkat.features.explore.domain.RouteRepository

class RouteRepositoryImpl() : RouteRepository {
    override suspend fun getRouteById(id: Long): Result<RouteModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getRouteByFilter(filters: List<String>): Result<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun findRouteByQuery(query: String?): Result<List<RouteModel>> {
        TODO("Not yet implemented")
    }
}