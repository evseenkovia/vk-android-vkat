package com.example.vk_android_vkat.features.explore.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface RouteService {
    @GET("/routes")
    suspend fun getRoutes(): Response<RouteResponse>
}
