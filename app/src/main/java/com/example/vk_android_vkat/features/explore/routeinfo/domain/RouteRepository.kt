package com.example.vk_android_vkat.features.explore.routeinfo.domain

import com.example.vk_android_vkat.domain.model.RouteModel

interface RouteRepository {
    suspend fun getRouteById(id: Long): RouteModel?
}