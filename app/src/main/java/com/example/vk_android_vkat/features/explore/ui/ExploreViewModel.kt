package com.example.vk_android_vkat.features.explore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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


class ExploreViewModelFactory(
    private val routeRepository: RouteRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExploreViewModel::class.java)) {
            return ExploreViewModel(routeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ExploreViewModel(
    val repository: RouteRepository
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
                _state.update { it.copy(searchQuery = "", error = null, isLoading = true, filters = RouteFilter()) }
                loadRoutes()
                event.onComplete()
            }
            ExploreEvent.ClearFilters -> {
                _state.update { it.copy(filters = RouteFilter()) }
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

            delay(delayTime)

            val routes = repository.getRoutes()
            routes
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

    // Текстовый поиск маршрута по названию
    fun findRouteByQuery(query: String?) {
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
