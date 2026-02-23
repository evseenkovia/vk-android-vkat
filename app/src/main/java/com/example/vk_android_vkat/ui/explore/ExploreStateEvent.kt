package com.example.vk_android_vkat.ui.explore

import com.example.vk_android_vkat.domain.model.RouteModel

data class ExploreState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val routes: List<RouteModel> = emptyList(),
    val filters: List<FilterItem> = defaultFilters()
)
sealed interface ExploreEvent {
    data class FilterClicked(val filter: FilterItem) : ExploreEvent
    data class RouteClicked(val routeId: Long) : ExploreEvent
    object Retry : ExploreEvent
}