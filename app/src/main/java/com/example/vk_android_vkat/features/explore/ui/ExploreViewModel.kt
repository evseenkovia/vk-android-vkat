package com.example.vk_android_vkat.features.explore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.features.explore.domain.RouteRepository
import com.example.vk_android_vkat.features.explore.domain.filter.RouteFilter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val repository: RouteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ExploreState())
    val state: StateFlow<ExploreState> = _state

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
                _state.update { it.copy(searchQuery = event.query) }
                searchQueryFlow.tryEmit(event.query)
            }

            is ExploreEvent.FiltersChanged -> {
                _state.update { it.copy(filters = event.newFilters) }
            }

            ExploreEvent.ApplyFilters -> {
                _state.update { it.copy(isFiltering = true) }
                loadFilteredRoutes()
            }

            is ExploreEvent.Retry -> {
                _state.update {
                    it.copy(
                        searchQuery = "",
                        error = null,
                        isLoading = true,
                        filters = RouteFilter()
                    )
                }
                loadRoutes()
                event.onComplete()
            }

            ExploreEvent.ClearFilters -> {
                _state.update { it.copy(filters = RouteFilter()) }
            }

            is ExploreEvent.ToggleFavourite -> toggleFavourite(event.routeId)
            ExploreEvent.ShowFavourites -> {
                switchToFavouriteMode()
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

            // Находим маршрут для добавления в Избранное
            val routeToUpdate = _state.value.routeList.find {
                it.id == routeId
            }

            routeToUpdate?.let { route ->
                val updatedRoute = route.copy(isFavourite = !route.isFavourite)

                // Сохраняем/удаляем маршрут локально
                if (updatedRoute.isFavourite)
                    repository.addRouteToFavourites(updatedRoute)
                else
                    repository.deleteFromFavourites(updatedRoute.id)

                // Обновляем состояние
                _state.update { currentState ->
                    val newList = currentState.routeList.map {
                        if (it.id == routeId) updatedRoute else it
                    }
                    currentState.copy(routeList = newList)
                }
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

    private fun setupSearchDebounce() {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(latency)
                .distinctUntilChanged()
                .collectLatest { query ->
                    findRouteByQuery(query)
                }
        }
    }

    // Загрузка маршрутов
    fun loadRoutes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val loadedRoutes = repository.getAllRoutes()

            loadedRoutes
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
                            error = exception.message ?: "Неизвестная ошибка",
                            isLoading = false
                        )
                    }
                }
        }
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
