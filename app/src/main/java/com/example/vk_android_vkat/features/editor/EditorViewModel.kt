package com.example.vk_android_vkat.features.editor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.features.editor.domain.RoutePointModel
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class EditorViewModel : ViewModel() {

    private val _state = MutableStateFlow(EditorState())
    val state: StateFlow<EditorState> = _state.asStateFlow()

    private val _effect = Channel<EditorEffect>()
    val effect = _effect.receiveAsFlow()

    private val searchManager = SearchFactory.getInstance()
        .createSearchManager(SearchManagerType.COMBINED)

    fun onEvent(event: EditorEvent) {
        when (event) {
            is EditorEvent.RouteNameChanged -> {
                _state.update { it.copy(routeName = event.value) }
            }

            is EditorEvent.RouteDescriptionChanged -> {
                _state.update { it.copy(routeDescription = event.value) }
            }

            is EditorEvent.ImageSelected -> {
                _state.update { it.copy(selectedImageUri = event.uri) }
            }

            is EditorEvent.DraftPointSelected -> {
                _state.update { it.copy(draftPoint = event.point) }
            }

            is EditorEvent.ConfirmMapPoint -> {
                Log.d("mymaps","got1")
                val draft = _state.value.draftPoint ?: return
                Log.d("mymaps","got2")

                viewModelScope.launch {
                    val address = searchAddress(Point(draft.latitude, draft.longitude))

                    _state.update { current ->
                        current.copy(
                            draftPoint = draft.copy(address = address)
                        )
                    }

                    _effect.send(EditorEffect.NavigateToEditPoint)
                }
            }

            is EditorEvent.ConfirmDraftPoint -> {
                _state.value.draftPoint?.let { draft ->
                    _state.update { current ->
                        current.copy(
                            points = current.points + event.point.orElse(draft),
                            draftPoint = null
                        )
                    }
                }
            }

            is EditorEvent.RemovePoint -> {
                _state.update { current ->
                    current.copy(
                        points = current.points.filterIndexed { index, _ -> index != event.index }
                    )
                }
            }
        }
    }

    private fun RoutePointModel?.orElse(other: RoutePointModel): RoutePointModel = this ?: other
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
                    Log.e("null", "Search error: $error")
                    cont.resume("")
                }
            })
            cont.invokeOnCancellation {
                session.cancel()
            }
        }
    }
}