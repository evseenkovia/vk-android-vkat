package com.example.vk_android_vkat.features.auth.data

class AuthRepositoryImpl(
    val authService: AuthService,
    val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(login: String, password: String): Result<Unit> {
        val request = AuthRequest(login, password)
        val response = authService.login(request)
        if (response.isSuccessful) {
            // Сохраняем токен после успешного логина
            response.body()?.token?.let {
                tokenManager.saveAuthData(
                    accessToken = it,
                    login = login
                )
            }
            return Result.success(Unit)
        } else {
            val errorMsg = response.errorBody().toString()
            return Result.failure(Exception(errorMsg))
        }
    }

    override suspend fun signUp(login: String, password: String): Result<Unit> {
        val request = AuthRequest(login, password)
        val response = authService.signUp(request)

        if (response.isSuccessful) {
            // Сохраняем токен после успешной регистрации
            response.body()?.token?.let {
                tokenManager.saveAuthData(
                    accessToken = it,
                    login = login
                )
            }
            return Result.success(Unit)
        } else {
            val errorMsg = response.errorBody().toString()
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