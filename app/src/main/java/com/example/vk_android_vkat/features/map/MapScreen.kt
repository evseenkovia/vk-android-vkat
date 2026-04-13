package com.example.vk_android_vkat.features.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PointF
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.features.editor.EditorEvent
import com.example.vk_android_vkat.features.editor.EditorState
import com.example.vk_android_vkat.features.editor.domain.RoutePointModel
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.location.Purpose
import com.yandex.mapkit.location.SubscriptionSettings
import com.yandex.mapkit.location.UseInBackground
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.image.ImageProvider

@Composable
fun MapScreen(
    state: MapState,
    onEvent: (MapEvent) -> Unit,
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val map = mapView.mapWindow.map

    // Состояние камеры
    var cameraLat by rememberSaveable { mutableDoubleStateOf(55.753089) }
    var cameraLon by rememberSaveable { mutableDoubleStateOf(37.622651) }
    var cameraZoom by rememberSaveable { mutableFloatStateOf(10f) }
    var initialLocationSet by rememberSaveable { mutableStateOf(false) }

    val defaultZoom = 15f

    fun setLocation(point: Point) {
        cameraLat = point.latitude
        cameraLon = point.longitude
        cameraZoom = defaultZoom
        initialLocationSet = true
    }

    // Локация пользователя
    var userLocationLayer by remember { mutableStateOf<UserLocationLayer?>(null) }
    val locationManager = remember {
        MapKitFactory.getInstance().createLocationManager()
    }

    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    DisposableEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            userLocationLayer = createUserLocationLayer(mapView)
        }

        val listener = object : LocationListener {
            override fun onLocationUpdated(location: com.yandex.mapkit.location.Location) {
                val point = location.position
                if (!initialLocationSet) {
                    setLocation(point)
                }
            }

            override fun onLocationStatusUpdated(status: LocationStatus) = Unit
        }

        val settings = SubscriptionSettings(
            UseInBackground.DISALLOW,
            Purpose.STATIC_DISPLAY_LOCATION
        )

        locationManager.subscribeForLocationUpdates(settings, listener)

        onDispose {
            locationManager.unsubscribe(listener)

            val currentCamera = map.cameraPosition
            cameraLat = currentCamera.target.latitude
            cameraLon = currentCamera.target.longitude
            cameraZoom = currentCamera.zoom
        }
    }

    LaunchedEffect(userLocationLayer) {
        userLocationLayer?.apply {
            setDefaultSource()
            isVisible = true
            isAutoZoomEnabled = true
            isHeadingModeActive = false
        }
    }

    DisposableEffect(Unit) {
        MapKitFactory.getInstance().onStart()
        mapView.onStart()

        onDispose {
            val currentCamera = map.cameraPosition
            cameraLat = currentCamera.target.latitude
            cameraLon = currentCamera.target.longitude
            cameraZoom = currentCamera.zoom

            mapView.onStop()
            MapKitFactory.getInstance().onStop()
        }
    }

    LaunchedEffect(cameraLat, cameraLon, cameraZoom) {
        map.move(
            CameraPosition(
                Point(cameraLat, cameraLon),
                cameraZoom,
                0f,
                0f
            )
        )
    }

    // Отображение точек
    LaunchedEffect(state.points) {
        if (!initialLocationSet && state.points.isNotEmpty()) {
            setLocation(
                Point(
                    state.points.last().latitude,
                    state.points.last().longitude
                )
            )
        }

        val mapObjects = map.mapObjects
        mapObjects.clear()

        state.points.forEachIndexed { index, point ->
            addPlacemark(
                mapObjects = mapObjects,
                point = Point(point.latitude, point.longitude),
                context = context,
                isSaved = true,
                isLast = index == state.points.lastIndex
            )
        }

    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { mapView })
    }
}

private fun createUserLocationLayer(mapView: MapView): UserLocationLayer {
    return MapKitFactory.getInstance().createUserLocationLayer(mapView.mapWindow)
}


private fun addPlacemark(
    mapObjects: MapObjectCollection,
    point: Point,
    context: Context,
    isSaved: Boolean,
    isLast: Boolean
): PlacemarkMapObject {
    val iconRes = when {
        isLast -> R.drawable.location_last
        isSaved -> R.drawable.location
        else -> R.drawable.location_current
    }

    return mapObjects.addPlacemark().apply {
        geometry = point
        setIcon(
            ImageProvider.fromResource(context, iconRes),
            IconStyle().apply {
                anchor = PointF(0.5f, 0.5f)
                flat = false
                scale = 0.4f
            }
        )
    }
}