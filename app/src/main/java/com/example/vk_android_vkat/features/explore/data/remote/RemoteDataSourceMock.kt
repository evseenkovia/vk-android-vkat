package com.example.vk_android_vkat.features.explore.data.remote

import com.example.vk_android_vkat.data.delayTime
import com.example.vk_android_vkat.data.mockRoutes
import com.example.vk_android_vkat.features.explore.domain.RouteModel
import kotlinx.coroutines.delay

class RemoteDataSourceMock: RemoteDataSource {
    override suspend fun getRouteById(id: Long): RouteModel? {
        delay(delayTime)
        return mockRoutes.find { it.id == id }
    }

    override suspend fun getRoutes(): List<RouteModel> {
        delay(delayTime)
        return mockRoutes
    }
}