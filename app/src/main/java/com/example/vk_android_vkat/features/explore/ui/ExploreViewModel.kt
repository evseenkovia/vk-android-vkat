package com.example.vk_android_vkat.features.explore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.features.explore.data.RouteRepositoryMock
import com.example.vk_android_vkat.features.explore.domain.RouteModel
import com.example.vk_android_vkat.features.explore.domain.RouteRepository
import com.example.vk_android_vkat.features.explore.domain.filter.RouteFilter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val repository: RouteRepository
) : ViewModel() {

    private val _filters = MutableStateFlow(RouteFilter())
    private val _searchQuery = MutableStateFlow("")
    private val _isFavouriteMode = MutableStateFlow(false)
    private val _isFiltering = MutableStateFlow(false)

    // Основной список маршрутов (реактивно из репозитория)
    private val baseRoutesFlow: StateFlow<List<RouteModel>> =
        repository.routesFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // UI состояние (объединяет базовый список с фильтрами и поиском)
    @OptIn(FlowPreview::class)
    val state: StateFlow<ExploreState> = combine(
        baseRoutesFlow,
        _filters,
        _searchQuery,
        _isFavouriteMode,
        _isFiltering
    ) { routes, filters, query, isFavouriteMode, isFiltering ->
        val filtered = when {
            isFavouriteMode -> routes.filter { it.isFavourite }
            query.isNotBlank() -> routes.filter { it.title.contains(query, ignoreCase = true) }
            isFiltering -> routes.filter {
                it.rating >= filters.rating.start && it.rating <= filters.rating.endInclusive &&
                        it.durationHours >= filters.duration.start && it.durationHours <= filters.duration.endInclusive &&
                        it.distanceKm >= filters.distance.start && it.distanceKm <= filters.distance.endInclusive
            }
            else -> routes
        }
        ExploreState(
            routeList = filtered,
            isLoading = false,
            error = null,
            searchQuery = query,
            filters = filters,
            isFavourite = isFavouriteMode,
            isFiltering = isFiltering
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExploreState(isLoading = true)
    )

    private val searchQueryFlow = MutableSharedFlow<String>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val delayTime = 500L

    init {
        _isFavouriteMode.value = false
        setupSearchDebounce()
    }

    fun onEvent(event: ExploreEvent) {
        when (event) {
            is ExploreEvent.QueryChanged -> {
                _searchQuery.value = event.query
                searchQueryFlow.tryEmit(event.query)
            }
            is ExploreEvent.FiltersChanged -> {
                _filters.value = event.newFilters
            }
            ExploreEvent.ApplyFilters -> {
                _isFiltering.value = true
            }
            is ExploreEvent.Retry -> {
                _searchQuery.value = ""
                _filters.value = RouteFilter()
                _isFiltering.value = false
                _isFavouriteMode.value = false
                event.onComplete()
            }
            ExploreEvent.ClearFilters -> {
                _filters.value = RouteFilter()
                _isFiltering.value = false
            }
            is ExploreEvent.ToggleFavourite -> toggleFavourite(event.routeId)
            ExploreEvent.ShowFavourites -> {
                _isFavouriteMode.value = true
                _isFiltering.value = false
                _searchQuery.value = ""
            }
        }
    }

    private fun toggleFavourite(routeId: Int) {
        viewModelScope.launch {
            val currentList = baseRoutesFlow.value
            val route = currentList.find { it.id == routeId } ?: return@launch
            val updatedRoute = route.copy(isFavourite = !route.isFavourite)

            if (updatedRoute.isFavourite) {
                repository.addRouteToFavourites(updatedRoute)
            } else {
                repository.deleteFromFavourites(updatedRoute.id)
            }

            // Мгновенно обновляем кеш в репозитории, чтобы UI перерисовался
            (repository as? RouteRepositoryMock)?.updateRouteFavouriteStatus(routeId, updatedRoute.isFavourite)
        }
    }

    @OptIn(FlowPreview::class)
    private fun setupSearchDebounce() {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(delayTime)
                .distinctUntilChanged()
                .collect { query ->
                    // уже обрабатывается через combine, дополнительно ничего не нужно
                }
        }
    }
}