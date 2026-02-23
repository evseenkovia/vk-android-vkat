package com.example.vk_android_vkat.features.explore.routeinfo.ui

import androidx.lifecycle.ViewModel
import com.example.vk_android_vkat.features.explore.ExploreViewModel
import com.example.vk_android_vkat.features.explore.routeinfo.domain.RouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RouteInfoViewModel(
    val repository : RouteRepository
) : ViewModel() {

    private val _state = MutableStateFlow<RouteInfoState>(RouteInfoState.Loading)
    val state: StateFlow<RouteInfoState> = _state

    fun onEvent(event: RouteInfoEvent){
        when(event) {
            RouteInfoEvent.onOpenInMapsClicked -> TODO("Открываем маршрут в картах")
            RouteInfoEvent.writeAReview -> TODO("вызываем окошко для доабвления отзыва")
        }
    }

    fun loadRoute(routeId: Long){
        val route = repository.getRouteById(routeId)
        if (route != null){
            _state.value = RouteInfoState.RouteInfoLoaded(route)
        } else {
            _state.value = RouteInfoState.Error("Маршрут не найден")
        }
    }
}