package com.example.vk_android_vkat.features.auth.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("signup")
    suspend fun signUp(@Body request: AuthRequest): Response<AuthResponse>
}