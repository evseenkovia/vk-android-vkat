package com.example.vk_android_vkat.features.profile.data

import android.util.Log
import com.example.vk_android_vkat.common.utils.TokenManager
import com.example.vk_android_vkat.features.profile.domain.VKUserInfo
import com.vk.id.VKID
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserFail
import com.vk.id.refreshuser.VKIDGetUserParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class VKUserRepository(
    private val tokenManager: TokenManager
) {
    companion object {
        private const val TAG = "VKUserRepository"
    }

    suspend fun getUserInfo(): VKUserInfo? {
        val accessToken = tokenManager.getAccessToken()
        Log.d(TAG, "AccessToken exists: ${accessToken != null}")

        if (accessToken == null) {
            Log.e(TAG, "AccessToken is null")
            return null
        }

        return suspendCancellableCoroutine { continuation ->
            Log.d(TAG, "Calling VKID.instance.getUserData()")

            // Запускаем suspend функцию внутри корутины
            val coroutineScope = CoroutineScope(Dispatchers.IO)
            coroutineScope.launch {
                try {
                    VKID.instance.getUserData(
                        callback = object : VKIDGetUserCallback {
                            override fun onSuccess(user: com.vk.id.VKIDUser) {
                                Log.d(TAG, "=== VKIDUser DATA ===")
                                Log.d(TAG, "firstName: ${user.firstName}")
                                Log.d(TAG, "lastName: ${user.lastName}")
                                Log.d(TAG, "avatar url: ${user.email}")
                                Log.d(TAG, "email: ${user.email}")
                                Log.d(TAG, "userId: ${user.email}")

                                val userInfo = VKUserInfo(
                                    id = 12345,
                                    firstName = user.firstName ?: "",
                                    lastName = user.lastName ?: "",
                                    avatarUrl = user.photo200 ?: user.photo100 ?: user.photo50,
                                    email = user.email,
                                    phone = null
                                )
                                continuation.resume(userInfo)
                            }

                            override fun onFail(fail: VKIDGetUserFail) {
                                Log.e(TAG, "Failed to get user data: $fail")
                                continuation.resume(null)
                            }
                        },
                        params = VKIDGetUserParams { }
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Exception: ${e.message}", e)
                    continuation.resume(null)
                }
            }

            continuation.invokeOnCancellation {
                coroutineScope.cancel()
            }
        }
    }
}
