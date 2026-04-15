package com.example.vk_android_vkat.features.auth.data

interface AuthRepository {
    suspend fun login(login: String, password: String) : Result<Unit>
    suspend fun signUp(login: String, password: String) : Result<Unit>

//    suspend fun passwordRecovery(login: String) : Result<T>

}