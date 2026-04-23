package com.example.vk_android_vkat.features.editor

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.features.editor.domain.RoutePointModel
import com.example.vk_android_vkat.features.explore.data.RouteRepositoryMock
import com.example.vk_android_vkat.features.explore.domain.RouteModel
import com.example.vk_android_vkat.features.explore.domain.RouteRepository
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
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
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume

class EditorViewModel(
    private val routeRepository: RouteRepository,
    private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(EditorState())
    val state: StateFlow<EditorState> = _state.asStateFlow()

    private val _effect = Channel<EditorEffect>()
    val effect = _effect.receiveAsFlow()

    private val searchManager = SearchFactory.getInstance()
        .createSearchManager(SearchManagerType.COMBINED)

    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val fileName = "route_${System.currentTimeMillis()}.jpg"
            val imagesDir = File(context.filesDir, "route_images")
            if (!imagesDir.exists()) imagesDir.mkdirs()
            val outputFile = File(imagesDir, fileName)
            FileOutputStream(outputFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            outputFile.absolutePath
        } catch (e: Exception) {
            Log.e("EditorViewModel", "Failed to save image", e)
            null
        }
    }

    fun onEvent(event: EditorEvent) {
        when (event) {
            is EditorEvent.RouteNameChanged -> {
                _state.update { it.copy(routeName = event.value) }
            }

            is EditorEvent.RouteDescriptionChanged -> {
                _state.update { it.copy(routeDescription = event.value) }
            }

            is EditorEvent.ImageSelected -> {
                val savedPath = event.uriString?.let { uriString ->
                    val uri = Uri.parse(uriString)
                    saveImageToInternalStorage(uri)
                }
                _state.update { it.copy(selectedImageUri = savedPath) }
            }

            is EditorEvent.DraftPointSelected -> {
                _state.update { it.copy(draftPoint = event.point) }
            }

            is EditorEvent.ConfirmMapPoint -> {
                val draft = _state.value.draftPoint ?: return

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

            // Новое событие завершения создания маршрута
            is EditorEvent.FinishRouteCreation -> {
                Log.d("EditorVM", "Received tags: ${event.selectedTags}")
                viewModelScope.launch {
                    val newRoute = createRouteFromState(event.selectedTags)
                    Log.d("EditorVM", "Created route with tags: ${newRoute.tags}")
                    routeRepository.addRoute(newRoute)
                    _effect.send(EditorEffect.NavigateBackToExplore)
                }
            }
        }
    }

    private fun RoutePointModel?.orElse(other: RoutePointModel): RoutePointModel = this ?: other

    private suspend fun searchAddress(point: Point): String {
        val options = SearchOptions().apply {
            searchTypes = SearchType.GEO.value
        }
        return kotlinx.coroutines.suspendCancellableCoroutine { cont ->
            val session = searchManager.submit(point, 16, options, object : Session.SearchListener {
                override fun onSearchResponse(response: com.yandex.mapkit.search.Response) {
                    val first = response.collection.children.firstOrNull()?.obj
                    val address = first?.name ?: first?.descriptionText ?: ""
                    if (cont.isActive) {
                        cont.resume(address)
                    }
                }
                override fun onSearchError(error: com.yandex.runtime.Error) {
                    Log.e("EditorViewModel", "Search error: $error")
                    if (cont.isActive) {
                        cont.resume("")
                    }
                }
            })
            cont.invokeOnCancellation {
                session.cancel()
            }
        }
    }

    /**
     * Создаёт объект RouteModel на основе текущего состояния редактора и выбранных тегов.
     */
    private fun createRouteFromState(selectedTags: Set<String>): RouteModel {
        val currentState = _state.value
        return RouteModel(
            id = routeRepository.getNextId(),
            title = currentState.routeName.trim(),
            description = currentState.routeDescription.trim(),
            distanceKm = 0,                     // заглушка
            durationHours = 0,                  // заглушка
            pointsCount = currentState.points.size,
            rating = 0f,
            imageUrl = currentState.selectedImageUri.toString(),
            tags = selectedTags.toList(),
            points = currentState.points,
            isFavourite = true
        )
    }
}