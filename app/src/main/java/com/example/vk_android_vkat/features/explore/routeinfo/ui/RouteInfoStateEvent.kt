package com.example.vk_android_vkat.features.explore.routeinfo.ui

import com.example.vk_android_vkat.features.explore.domain.RouteModel

// Состояние экрана
data class RouteInfoState(
    val error: String? = null,
    val isLoading: Boolean = true,
    val routeData: RouteModel? = null
)

// Действия пользователя
sealed interface RouteInfoEvent {
    object OpenInMapsClicked : RouteInfoEvent
    object AddCommentClicked : RouteInfoEvent
    object Refresh : RouteInfoEvent
    object ToggleFavourite : RouteInfoEvent
    object BackClicked : RouteInfoEvent
}

// Одноразовые события навигации/сообщений
sealed class RouteInfoEffect {
    object NavigateBack : RouteInfoEffect()
    data class ShowMessage(val message: String) : RouteInfoEffect()
}
