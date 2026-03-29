package com.example.vk_android_vkat.features.editor

import android.net.Uri
import java.io.Serializable


data class RoutePointUi(
    val address: String = "",
    val pointName: String = "",
    val pointDescription: String = "",
    val photoUri: Uri? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : Serializable

data class EditorState(
    val routeName: String = "",
    val routeDescription: String = "",
    val selectedImageUri: Uri? = null,
    val points: List<RoutePointUi> = emptyList()
)

sealed interface EditorEvent {
    data class RouteNameChanged(val value: String) : EditorEvent
    data class RouteDescriptionChanged(val value: String) : EditorEvent
    data class ImageSelected(val uri: Uri?) : EditorEvent
    data class PointAdded(val point: RoutePointUi?) : EditorEvent
    data object AddPointClicked : EditorEvent
    data class RemovePoint(val index: Int) : EditorEvent
}

sealed interface EditorEffect {
    data object NavigateToEditMap : EditorEffect
}