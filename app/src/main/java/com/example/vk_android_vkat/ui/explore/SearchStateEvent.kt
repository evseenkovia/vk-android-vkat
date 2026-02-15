package com.example.vk_android_vkat.ui.explore

import com.example.vk_android_vkat.domain.model.RouteModel

data class SearchUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val routes: List<RouteModel> = emptyList(),
    val filters: List<FilterItem> = defaultFilters()
)
sealed interface SearchUiEvent {
    data class FilterClicked(val filter: FilterItem) : SearchUiEvent
    data class RouteClicked(val routeId: Long) : SearchUiEvent
    object Retry : SearchUiEvent
}