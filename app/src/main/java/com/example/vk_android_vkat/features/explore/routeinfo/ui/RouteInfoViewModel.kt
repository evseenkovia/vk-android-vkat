package com.example.vk_android_vkat.features.explore.routeinfo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.features.explore.data.RouteRepositoryMock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RouteInfoViewModel (
    private val routeId: Long,
    private val repository: RouteRepositoryMock
) : ViewModel() {

    private val _state = MutableStateFlow<RouteInfoState>(RouteInfoState.Loading)
    val state: StateFlow<RouteInfoState> = _state

    init {
        loadRoute()
    }

    fun onEvent(event: RouteInfoEvent){
        when(event) {
            RouteInfoEvent.onOpenInMapsClicked -> TODO("Открываем маршрут в картах")
            RouteInfoEvent.writeAReview -> TODO("вызываем окошко для доабвления отзыва")
        }
    }

    fun loadRoute(){
        viewModelScope.launch {
            val route = repository.getRouteById(routeId)
            println("DEBUG: routeId=$routeId, found route=$route")
            _state.value = if (route != null){
                RouteInfoState.RouteInfoLoaded(route)
            } else {
                RouteInfoState.Error("Маршрут не найден")
            }
        }
    }
}