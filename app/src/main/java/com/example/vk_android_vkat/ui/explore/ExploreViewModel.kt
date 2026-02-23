package com.example.vk_android_vkat.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.data.mockRoutes
import com.example.vk_android_vkat.domain.model.RouteModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel(){

    private val _state = MutableStateFlow(ExploreState(isLoading = true))
    val state: StateFlow<ExploreState> = _state

    private val delayTime = 1000L

    init {
        loadRoutes()
    }

    // Обработка событий с UI
    fun onEvent(event: ExploreEvent){
        when(event) {
            is ExploreEvent.FilterClicked -> TODO()
            ExploreEvent.Retry -> TODO()
            is ExploreEvent.RouteClicked -> TODO()
        }
    }

    fun loadRoutes(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            delay(delayTime)
            try {
                val loadedRoutes: List<RouteModel> = mockRoutes
                _state.update { it.copy(routes = loadedRoutes, isLoading = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message ?: "Unknown error.",
                        routes = emptyList(),
                        isLoading = false)
                }
            }
        }
    }
}
