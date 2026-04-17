package com.example.vk_android_vkat.features.explore.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RouteService {
    @GET("/routes")
    suspend fun getRoutes(): Response<RouteResponse>

    @GET("routes/{id}")
    suspend fun getRouteById(@Path("id") id: Int): Response<RouteDto>


}
