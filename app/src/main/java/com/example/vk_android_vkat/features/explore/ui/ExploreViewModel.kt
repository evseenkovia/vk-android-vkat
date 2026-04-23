package com.example.vk_android_vkat.features.explore.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.features.explore.data.RouteRepositoryMock
import com.example.vk_android_vkat.features.explore.domain.RouteModel
import com.example.vk_android_vkat.features.explore.domain.RouteRepository
import com.example.vk_android_vkat.features.explore.domain.filter.RouteFilter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
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
        // Применяем режим "Избранное" или фильтры/поиск
        val filtered = when {
            isFavouriteMode -> routes.filter { it.isFavourite }
            query.isNotBlank() -> routes.filter { it.title.contains(query, ignoreCase = true) }
            isFiltering -> repository.filterRoutes(routes, filters) // можно вынести логику фильтрации
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
    private val latency = 500L

    init {
        loadRoutes()
        setupSearchDebounce()
    }

    // Обработка событий с UI
    @OptIn(FlowPreview::class)
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
                // Сброс всех временных состояний
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

    // Переходим в режим Избранное (все запросы и маршруты фильтруются)
    private fun switchToFavouriteMode() {
        viewModelScope.launch {
            _state.update { it.copy(isFavourite = true, error = null) }
            val favouriteRoutes = repository.getAllFavourites()
            favouriteRoutes
                .onSuccess { routes ->
                    _state.update {
                        it.copy(
                            routeList = routes,
                            isLoading = false
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            routeList = emptyList(),
                            error = exception.message,
                            isLoading = false
                        )
                    }
                }
        }
    }

    // Добавляем маршрут в Избранное
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
        }
    }

    private fun loadFilteredRoutes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            delay(delayTime)

            val filters = _state.value.filters
            val filterResult = repository.getRouteByFilter(filters)
            filterResult
                .onSuccess { routes ->
                    _state.update {
                        it.copy(
                            routeList = routes,
                            error = null,
                            isFiltering = false,
                            isLoading = false
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            error = exception.message ?: "Неизвестная ошибка",
                            isFiltering = false,
                            isLoading = false
                        )
                    }
                }
        }
    }

    @OptIn(FlowPreview::class)
    private fun setupSearchDebounce() {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(delayTime)
                .distinctUntilChanged()
                .collect { query ->
                    _searchQuery.value = query
                    _isFiltering.value = false   // поиск отключает режим фильтрации
                    _isFavouriteMode.value = false
                }
        }
    }

    init {
        _isFavouriteMode.value = false
    }

    // Текстовый поиск маршрута по названию
    private fun findRouteByQuery(query: String?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            delay(delayTime)

            val searchResult = repository.findRouteByQuery(query)
            searchResult
                .onSuccess { routes ->
                    _state.update {
                        it.copy(
                            routeList = routes,
                            isLoading = false
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            error = exception.message,
                            routeList = emptyList(),
                            isLoading = false
                        )
                    }
                }
        }
    }
}
