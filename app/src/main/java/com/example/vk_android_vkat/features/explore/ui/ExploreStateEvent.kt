package com.example.vk_android_vkat.features.explore.ui

import com.example.vk_android_vkat.domain.model.RouteModel
import com.example.vk_android_vkat.features.explore.FilterItem
import com.example.vk_android_vkat.features.explore.defaultFilters

sealed interface ExploreState {
    object Loading: ExploreState
    data class Error(val message: String): ExploreState
    data class Routes(val data: List<RouteModel> = emptyList()) : ExploreState
    data class Filters(val filters : List<FilterItem> = defaultFilters()) : ExploreState
}
sealed interface ExploreEvent {
    data class FilterClicked(val filter: FilterItem) : ExploreEvent

    data class ToggleFavourite(val routeId: Long) : ExploreEvent
    object Retry : ExploreEvent
}