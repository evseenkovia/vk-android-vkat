package com.example.vk_android_vkat.features.explore.routeinfo.ui

import com.example.vk_android_vkat.domain.model.RouteModel

// Состояние экрана
sealed interface RouteInfoState {
    data class Error(val message: String) : RouteInfoState
    object Loading : RouteInfoState
    data class RouteInfoLoaded(val route: RouteModel) : RouteInfoState
}

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
