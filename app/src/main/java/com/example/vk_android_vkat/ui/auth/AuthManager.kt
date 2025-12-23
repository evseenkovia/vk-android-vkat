package com.example.vk_android_vkat.ui.auth

import android.content.Context
import androidx.core.content.edit

// core/auth/AuthManager.kt
class AuthManager private constructor(private val context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: AuthManager? = null

        fun getInstance(context: Context): AuthManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        // Проверяем наличие токена (или другого маркера авторизации)
        return getAuthToken() != null
    }

    fun login(token: String) {
        prefs.edit { putString("auth_token", token) }
    }

    fun logout() {
        prefs.edit { remove("auth_token") }
    }

    private fun getAuthToken(): String? {
        return prefs.getString("auth_token", null)
    }
}