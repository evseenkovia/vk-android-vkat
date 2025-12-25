package com.example.vk_android_vkat.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.R

import com.example.vk_android_vkat.domain.model.RouteUi
import com.example.vk_android_vkat.mock_data.delayTime
import com.example.vk_android_vkat.mock_data.mockRoutes
import com.example.vk_android_vkat.ui.favourite.FavouriteDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val favouriteDataStore: FavouriteDataStore
) : ViewModel() {

    private val _routes = MutableStateFlow<List<RouteUi>?>(null)
    val routes: StateFlow<List<RouteUi>?> = _routes.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var cachedRoutes: List<RouteUi> = emptyList()
    private var favouriteIds: Set<Long> = emptySet()

    init {
        observeFavourites()
        loadRoutes()
    }

    private fun observeFavourites() {
        viewModelScope.launch {
            favouriteDataStore.favouritesFlow.collect { ids ->
                favouriteIds = ids
                updateRoutes()
            }
        }
    }

    fun loadRoutes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            delay(delayTime)

            try {
                val shouldFail = false
                if (shouldFail) throw Exception("Ошибка загрузки маршрутов")

                cachedRoutes = mockRoutes
                updateRoutes()

            } catch (e: Exception) {
                _error.value = e.message ?: R.string.unknown_error.toString()
                cachedRoutes = emptyList()
                _routes.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun updateRoutes() {
        _routes.value = cachedRoutes.map { route ->
            route.copy(isFavourite = favouriteIds.contains(route.id))
        }
    }

    fun toggleFavourite(routeId: Long) {
        viewModelScope.launch {
            favouriteDataStore.toggleFavourite(routeId)
        }
    }
}
