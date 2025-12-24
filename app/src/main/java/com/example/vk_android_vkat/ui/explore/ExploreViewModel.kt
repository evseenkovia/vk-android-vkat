package com.example.vk_android_vkat.ui.explore

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.domain.model.RouteUi
import com.example.vk_android_vkat.mock_data.delayTime
import com.example.vk_android_vkat.mock_data.mockRoutes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel(){

    private val _routes = MutableStateFlow<List<RouteUi>?>(null)
    val routes: StateFlow<List<RouteUi>?> = _routes.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadRoutes()
    }

    fun loadRoutes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            delay(delayTime)
            try {
                // Можно случайно сгенерировать ошибку для проверки
                val shouldFail = false
                if (shouldFail) throw Exception("Ошибка загрузки маршрутов")

                val loadedRoutes = mockRoutes   // Полный список
//                val loadedRoutes = null         // Пустой список
                _routes.value = loadedRoutes
            } catch (e: Exception) {
                _error.value = (e.message ?: R.string.unknown_error).toString()
                _routes.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
