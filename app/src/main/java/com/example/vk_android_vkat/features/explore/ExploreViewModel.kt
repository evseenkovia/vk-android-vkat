package com.example.vk_android_vkat.features.explore

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

    private val _state = MutableStateFlow<ExploreState>(ExploreState.Loading)
    val state: StateFlow<ExploreState> = _state

    private val delayTime = 1000L

    init {
        loadRoutes()
    }

    // Обработка событий с UI
    fun onEvent(event: ExploreEvent){
        when(event) {
            is ExploreEvent.FilterClicked -> TODO("Обработка списка маршрутов фильтрами")
            ExploreEvent.Retry -> TODO("Обновление списка маршрутов - еще один запрос на сервер")
        }
    }

    // Загрузка маршрутов
    fun loadRoutes(){
        viewModelScope.launch {
            _state.value = ExploreState.Loading
            delay(delayTime)
            try {
                val loadedRoutes: List<RouteModel> = mockRoutes
                _state.value = ExploreState.Routes(data = loadedRoutes)
            } catch (e: Exception) {
                _state.value = ExploreState.Error(message = e.message ?: "Unknown error")
            }
        }
    }

    // Поиск маршрута по id
    fun getRouteById(routeId: Long) : RouteModel? {
        return when(val s = _state.value){
            is ExploreState.Routes -> s.data.find { it.id == routeId }
            else -> null
        }
    }
}
