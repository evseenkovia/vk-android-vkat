package com.example.vk_android_vkat.features.explore.data.remote

import com.example.vk_android_vkat.features.explore.domain.RouteModel

interface RemoteDataSource {
    suspend fun getRouteById(id: Long): RouteModel?
    suspend fun getRoutes(): List<RouteModel>
}