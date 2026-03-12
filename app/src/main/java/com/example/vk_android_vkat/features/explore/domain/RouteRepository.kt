package com.example.vk_android_vkat.features.explore.domain

interface RouteRepository {
    suspend fun getRouteById(id: Long): Result<RouteModel>

    suspend fun getRouteByFilter(filters: List<String>): Result<Any>

    suspend fun findRouteByQuery(query: String?): Result<List<RouteModel>>
}