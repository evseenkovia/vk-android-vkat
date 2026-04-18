package com.example.vk_android_vkat.features.editor

import android.net.Uri
import com.example.vk_android_vkat.features.editor.domain.RoutePointModel


data class EditorState(
    val routeName: String = "",
    val routeDescription: String = "",
    val selectedImageUri: String? = null,
    val points: List<RoutePointModel> = emptyList(),
    val draftPoint: RoutePointModel? = null, // временно выбранная точка с карты
)

sealed interface EditorEvent {
    data class RouteNameChanged(val value: String) : EditorEvent
    data class RouteDescriptionChanged(val value: String) : EditorEvent
    data class ImageSelected(val uriString: String?) : EditorEvent
    data class DraftPointSelected(val point: RoutePointModel) : EditorEvent
    data object ConfirmMapPoint : EditorEvent
    data class ConfirmDraftPoint(val point: RoutePointModel?) : EditorEvent
    data class RemovePoint(val index: Int) : EditorEvent
    data class FinishRouteCreation(val selectedTags: Set<String>) : EditorEvent
}

sealed interface EditorEffect {
    data object NavigateToEditPoint : EditorEffect
    data object NavigateBackToExplore : EditorEffect
}