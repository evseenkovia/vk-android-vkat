package com.example.vk_android_vkat.features.explore.routeinfo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.features.explore.data.RouteRepositoryMock
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RouteInfoViewModel(
    private val routeId: Long, private val repository: RouteRepositoryMock
) : ViewModel() {

    private val _state = MutableStateFlow(RouteInfoState())
    val state: StateFlow<RouteInfoState> = _state

    private val _effect = Channel<RouteInfoEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadRoute()
    }

    // Загрузка данных маршрута по id
    private fun loadRoute() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val loadResult = repository.getRouteById(routeId)

            loadResult
                .onSuccess { route ->
                    println("DEBUG: routeId=$routeId, found route=${route}")
                    _state.update { it.copy(routeData = route, isLoading = false) }
                }
                .onFailure { exception ->
                    _state.update { it.copy(error = exception.message, routeData = null, isLoading = false) }
                }
        }
    }

    // Обработчик действий пользователя
    fun onEvent(event: RouteInfoEvent) {
        when (event) {
            RouteInfoEvent.Refresh -> loadRoute()
            RouteInfoEvent.ToggleFavourite -> toggleFavourite()
            RouteInfoEvent.OpenInMapsClicked -> TODO("Открываем маршрут в картах")
            RouteInfoEvent.AddCommentClicked -> TODO("вызываем окошко для доабвления отзыва")
            RouteInfoEvent.BackClicked -> navigateBack()
        }
    }

    private fun navigateBack() =
        viewModelScope.launch { _effect.send(RouteInfoEffect.NavigateBack) }

    // Добавляем маршрут в избранное
    private fun toggleFavourite() {
//        val current = _state.value
//        if (current is RouteInfoState) {
//            _state.value = current.copy(
//                route = current.route.copy(isFavourite = !current.route.isFavourite)
//            )
//        }
    }
}