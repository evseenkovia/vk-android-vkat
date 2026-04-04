package com.example.vk_android_vkat.features.editor.domain

import android.net.Uri

data class RoutePointModel(
    val address: String = "",
    val pointName: String = "",
    val pointDescription: String = "",
    val photoUri: Uri? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
