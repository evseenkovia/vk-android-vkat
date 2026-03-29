package com.example.vk_android_vkat.features.editor.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class MapViewModel : ViewModel() {

    private val _state = MutableStateFlow(MapState())
    val state: StateFlow<MapState> = _state

    private val _effect = MutableSharedFlow<MapEffect>()
    val effect: SharedFlow<MapEffect> = _effect

    private val searchManager = SearchFactory.getInstance()
        .createSearchManager(SearchManagerType.COMBINED)

    fun setInitialPoints(points: List<AddressPoint>) {
        _state.update { it.copy(placemarks = points.map { Point(it.latitude, it.longitude) }) }
    }

    fun reducer(event: MapEvent) {
        when(event) {
            is MapEvent.LongTap -> {
                _state.update { it.copy(lastPlacemark = event.cords) }
            }
            is MapEvent.CameraSave -> {
                _state.update { it.copy(cameraPosition = event.point, zoom = event.zoom) }
            }
            is MapEvent.ConfirmPlacemark -> {
                viewModelScope.launch {
                    val point = _state.value.lastPlacemark
                    if (point != null) {
                        val address = searchAddress(point)
                        _effect.emit(MapEffect.Chosen(point, address))
                    }
                }
            }
            is MapEvent.OnCurrentLocation -> {
                if (!state.value.initialLocationSet){
                    Log.d("MapViewModel", "OnCurrentLocation ${event.point.latitude}")
                    _state.update { it.copy(cameraPosition = event.point, zoom = 15f, initialLocationSet = true) }
                }
            }

        }
    }
    private suspend fun searchAddress(point: Point): String {
        val options = SearchOptions().apply {
            searchTypes = SearchType.GEO.value
        }
        return suspendCancellableCoroutine { cont ->
            val session = searchManager.submit(point, 16, options, object : Session.SearchListener {
                override fun onSearchResponse(response: com.yandex.mapkit.search.Response) {
                    val first = response.collection.children.firstOrNull()?.obj
                    val address = first?.name ?: first?.descriptionText ?: ""
                    cont.resume(address)
                }
                override fun onSearchError(error: Error) {
                    Log.e("MapViewModel", "Search error: $error")
                    cont.resume("")
                }
            })
            cont.invokeOnCancellation {
                session.cancel()
            }
        }
    }
}