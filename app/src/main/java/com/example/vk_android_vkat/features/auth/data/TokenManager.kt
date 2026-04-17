package com.example.vk_android_vkat.features.auth.data

import android.content.Context
import androidx.core.content.edit

/*
* TokenManager - класс для хранения токена авторизации
*/
class TokenManager(context: Context) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_LOGIN = "login"
    }

    fun saveAuthData(accessToken: String, login: String) {
        prefs.edit {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_LOGIN, login)
        }
    }

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)
    fun getLogin(): String? = prefs.getString(KEY_LOGIN, null)

    fun clear() {
        prefs.edit { clear() }
    }

    fun isLoggedIn(): Boolean = !getAccessToken().isNullOrEmpty()
}
