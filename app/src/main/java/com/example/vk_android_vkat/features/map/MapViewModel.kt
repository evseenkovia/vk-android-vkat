package com.example.vk_android_vkat.features.map

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.features.explore.domain.RouteRepository

import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MapViewModel(
    private val repository: RouteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MapState())
    val state: StateFlow<MapState> = _state.asStateFlow()

    init {
        loadMarkers()
    }


    private fun loadMarkers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = repository.getRouteStartPoints()

            result
                .onSuccess { markers ->
                    _state.update {
                        it.copy(
                            markers = markers,
                            isLoading = false
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            error = exception.message ?: "Не удалось загрузить метки",
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun refresh() {
        loadMarkers()
    }
}