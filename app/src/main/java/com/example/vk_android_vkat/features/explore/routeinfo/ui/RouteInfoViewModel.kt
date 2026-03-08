package com.example.vk_android_vkat.features.explore.routeinfo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.features.explore.data.RouteRepositoryMock
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RouteInfoViewModel (
    private val routeId: Long,
    private val repository: RouteRepositoryMock
) : ViewModel() {

    private val _state = MutableStateFlow<RouteInfoState>(RouteInfoState.Loading)
    val state: StateFlow<RouteInfoState> = _state

    private val _effect = Channel<RouteInfoEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadRoute()
    }

    private fun loadRoute(){
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

    // Обработчик действий пользователя
    fun onEvent(event: RouteInfoEvent){
        when(event) {
            RouteInfoEvent.Refresh -> loadRoute()
            RouteInfoEvent.ToggleFavourite -> toggleFavourite()
            RouteInfoEvent.OpenInMapsClicked -> TODO("Открываем маршрут в картах")
            RouteInfoEvent.AddCommentClicked -> TODO("вызываем окошко для доабвления отзыва")
            RouteInfoEvent.BackClicked -> navigateBack()
        }
    }

    private fun navigateBack() = viewModelScope.launch { _effect.send(RouteInfoEffect.NavigateBack) }

    // Добавляем маршрут в избранное
    private fun toggleFavourite() {
        val current = _state.value
        if (current is RouteInfoState.RouteInfoLoaded) {
            _state.value = current.copy(
                route = current.route.copy(isFavourite = !current.route.isFavourite)
            )
        }
    }
}