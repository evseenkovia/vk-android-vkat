package com.example.vk_android_vkat.features.explore.ui

import com.example.vk_android_vkat.features.explore.domain.RouteModel
import com.example.vk_android_vkat.features.explore.FilterItem
import com.example.vk_android_vkat.features.explore.defaultFilters

data class ExploreState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val routeList: List<RouteModel> = emptyList(),
    val searchQuery: String = "",
    val filters: List<FilterItem> = defaultFilters()
)

sealed interface ExploreEvent {
    data class QueryChanged(val query: String): ExploreEvent
    data class FilterClicked(val filter: FilterItem) : ExploreEvent
    object Retry : ExploreEvent
}