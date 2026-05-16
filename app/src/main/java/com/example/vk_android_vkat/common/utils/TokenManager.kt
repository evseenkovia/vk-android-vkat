package com.example.vk_android_vkat.common.utils

import android.content.Context
import android.content.SharedPreferences
import com.vk.id.AccessToken
import androidx.core.content.edit

class TokenManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("vk_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_EXPIRES_IN = "expires_in"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun saveToken(accessToken: AccessToken) {
        prefs.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken.token)
            putInt(KEY_USER_ID, accessToken.userID.toInt())
            putLong(KEY_EXPIRES_IN, accessToken.expireTime)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)

    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, 0)

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun clearToken() {
        prefs.edit { clear() }
    }
}
