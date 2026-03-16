package com.example.vk_android_vkat.features.explore.ui

import com.example.vk_android_vkat.features.explore.domain.RouteModel
import com.example.vk_android_vkat.features.explore.FilterItem
import com.example.vk_android_vkat.features.explore.domain.filter.RouteFilter

data class ExploreState(
    val isLoading : Boolean = true,
    val error : String? = null,
    val routeList : List<RouteModel> = emptyList(),
    val searchQuery : String = "",
    val filters : RouteFilter = RouteFilter(),     // фильтры по умолчанию
    val isFiltering : Boolean = false,
    val isFavourite : Boolean = false
)

sealed interface ExploreEvent {
    data class QueryChanged(val query: String) : ExploreEvent
    data class ToggleFavourite(val routeId: Int) : ExploreEvent
    object ShowFavourites : ExploreEvent
    data class FiltersChanged(val newFilters: RouteFilter) : ExploreEvent
    object ApplyFilters : ExploreEvent
    object ClearFilters : ExploreEvent
    data class Retry(val onComplete : () -> Unit) : ExploreEvent
}