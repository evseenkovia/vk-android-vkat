package com.example.vk_android_vkat.features.auth.data

class AuthRepositoryImpl(
    val authService: AuthService
) : AuthRepository {

    override suspend fun login(
        login: String,
        password: String
    ): Result<String> {
        val request = AuthRequest(login, password)
        println("ОТПРАВЛЯЮ: $request")
        val response = authService.login(request)

        if (response.isSuccessful) {
            println("УСПЕХ: токен получен")
            return Result.success(response.body()?.token ?: "")
        } else {
            val errorMsg = response.body()?.toString() ?: "Unknown error"
            println("ОШИБКА СЕРВЕРА (код ${response.code()}): $errorMsg")
            return Result.failure(Exception(errorMsg))
        }
    }

    override suspend fun signUp(
        login: String,
        password: String
    ): Result<String> {
        val request = AuthRequest(login, password)
        val response = authService.signUp(request)

        if (response.isSuccessful) {
            return Result.success(response.body()?.token ?: "")
        } else {
            val errorMsg = response.body()?.toString() ?: "Unknown error"
            return Result.failure(Exception(errorMsg))
        }
    }

//    override suspend fun login(login: String, password: String): Result<AuthResponse> {
//        return safeApiCall<AuthResponse> {
//            authService.login(AuthRequest(login, password))
//        }
//    }
//
//    override suspend fun signUp(login: String, password: String): Result<AuthResponse> {
//        return safeApiCall<AuthResponse> {
//            authService.signUp(AuthRequest(login, password))
//        }
//    }
//
//    private inline fun <reified T> safeApiCall(
//        call: () -> Response<T>
//    ): Result<T> {
//        return try {
//            val response = call()
//            if (response.isSuccessful) {
//                val body = response.body()
//                if (body != null) {
//                    Result.success(body)
//                } else {
//                    Result.failure(Exception("Empty body"))
//                }
//            } else {
//                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
//                Result.failure(Exception(errorMsg))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
}