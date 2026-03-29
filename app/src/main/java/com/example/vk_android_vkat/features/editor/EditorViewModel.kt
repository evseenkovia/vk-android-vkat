package com.example.vk_android_vkat.features.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditorViewModel : ViewModel() {

    private val _state = MutableStateFlow(EditorState())
    val state: StateFlow<EditorState> = _state.asStateFlow()

    private val _effect = Channel<EditorEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

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
            is EditorEvent.AddPointClicked -> {
                val current = _state.value
                viewModelScope.launch {
                    _effect.send(
                        EditorEffect.NavigateToEditMap
                    )
                }
            }
            is EditorEvent.PointAdded -> {
                event.point?.let { newPoint ->
                    _state.update { current ->
                        current.copy(
                            points = current.points + newPoint
                        )
                    }
                }
            }
            is EditorEvent.RemovePoint -> {
                _state.update { current ->
                    current.copy(
                        points = current.points.filterIndexed { i, _ -> i != event.index }
                    )
                }
            }
        }
    }
}