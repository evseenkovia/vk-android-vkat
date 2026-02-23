package com.example.vk_android_vkat.features.explore

import com.example.vk_android_vkat.data.mockRoutes
import com.example.vk_android_vkat.domain.model.RouteModel

sealed interface ExploreState {
    object Loading: ExploreState
    data class Error(val message: String): ExploreState
    data class Routes(val data: List<RouteModel> = emptyList()) : ExploreState
    data class Filters(val filters : List<FilterItem> = defaultFilters()) : ExploreState
}
sealed interface ExploreEvent {
    data class FilterClicked(val filter: FilterItem) : ExploreEvent
    object Retry : ExploreEvent
}