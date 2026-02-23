package com.example.vk_android_vkat.features.explore.routeinfo.ui

import com.example.vk_android_vkat.domain.model.RouteModel

sealed interface RouteInfoState {
    data class Error(val message: String) : RouteInfoState
    object Loading : RouteInfoState
    data class RouteInfoLoaded(val route: RouteModel) : RouteInfoState
}

sealed interface RouteInfoEvent {
    object onOpenInMapsClicked : RouteInfoEvent
    object writeAReview : RouteInfoEvent
}
