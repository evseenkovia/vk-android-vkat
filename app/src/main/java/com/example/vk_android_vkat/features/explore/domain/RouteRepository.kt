package com.example.vk_android_vkat.features.explore.domain

import com.example.vk_android_vkat.features.explore.domain.filter.RouteFilter

interface RouteRepository {
    suspend fun getRouteById(id: Long): Result<RouteModel>

    suspend fun getRouteByFilter(filter: RouteFilter): Result<List<RouteModel>>

    suspend fun findRouteByQuery(query: String?): Result<List<RouteModel>>
}