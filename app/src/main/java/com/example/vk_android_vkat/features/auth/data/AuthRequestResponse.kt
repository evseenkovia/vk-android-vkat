package com.example.vk_android_vkat.features.auth.data

data class AuthRequest(
    val login: String,
    val password: String
)

data class AuthResponse(
    val token: String
)

////data class LoginRequest(
////    val login: String,
////    val password: String
////)
////
////data class LoginResponse(
////    val token: String
////)
////
////data class SignUpRequest(
////    val login: String,
////    val password: String
////)
////
////data class SignUpResponse(
////    val token: String
//)

// registerrequest + registerResponse - для них свои структуры
