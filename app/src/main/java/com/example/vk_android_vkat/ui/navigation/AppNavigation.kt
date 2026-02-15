package com.example.vk_android_vkat.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Search
@Serializable
object Favourite
@Serializable
object Editor
@Serializable
object Map
@Serializable
object Profile

@Serializable
data class RouteDetails(val routeId: Long)

@Serializable
object Settings
