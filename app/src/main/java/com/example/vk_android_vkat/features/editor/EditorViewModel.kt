package com.example.vk_android_vkat.features.editor

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vk_android_vkat.features.editor.domain.RoutePointModel
import com.example.vk_android_vkat.features.explore.domain.RouteModel
import com.example.vk_android_vkat.features.explore.domain.RouteRepository
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.Session
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.math.*

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
        val points = currentState.points

        val distanceMeters = DistanceUtils.approxWalkDist(points)
        val walkingTimeMinutes = DistanceUtils.estimatedWalkingTimeMinutes(points)

        return RouteModel(
            id = routeRepository.getNextId(),
            title = currentState.routeName.trim(),
            description = currentState.routeDescription.trim(),
            distanceKm = (distanceMeters / 1000.0).roundToInt(),
            durationHours = (walkingTimeMinutes / 60.0).roundToInt(),
            pointsCount = points.size,
            rating = 0f,
            imageUrl = currentState.selectedImageUri.toString(),
            tags = selectedTags.toList(),
            points = points,
            isFavourite = true,
            authorID = "1"
        )
    }
}



object DistanceUtils {
    private const val EARTH_RADIUS_METERS = 6_371_000.0
    const val STREETS_FACTOR = 1.2
    const val AVERAGE_WALKING_SPEED_MPS = 5_000.0 / 3_600.0
    fun distanceBetween(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS_METERS * c
    }

    fun totalRouteDistance(points: List<RoutePointModel>): Double {
        if (points.size < 2) return 0.0
        return points.zipWithNext { a, b -> a.distanceTo(b) }.sum()
    }

    fun approxWalkDist(points: List<RoutePointModel>): Double {
        return totalRouteDistance(points) * STREETS_FACTOR
    }
    fun estimatedWalkingTimeMinutes(points: List<RoutePointModel>): Double {
        val distanceMeters = approxWalkDist(points)
        val timeSeconds =  distanceMeters / AVERAGE_WALKING_SPEED_MPS
        return timeSeconds / 60.0
    }
}

fun RoutePointModel.distanceTo(other: RoutePointModel): Double =
    DistanceUtils.distanceBetween(latitude, longitude, other.latitude, other.longitude)