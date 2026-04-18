package com.example.vk_android_vkat.features.editor.domain

import android.net.Uri
import kotlinx.serialization.Serializable

@Serializable
data class RoutePointModel(
    val address: String = "",
    val pointName: String = "",
    val pointDescription: String = "",
    val photoUri: String? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
