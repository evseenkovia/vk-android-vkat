package com.example.vk_android_vkat.features.editor.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PointF
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.features.editor.EditorEvent
import com.example.vk_android_vkat.features.editor.EditorState
import com.example.vk_android_vkat.features.editor.domain.RoutePointModel
import com.example.vk_android_vkat.features.map.CameraState
import com.example.vk_android_vkat.features.map.moveMap
import com.example.vk_android_vkat.features.map.rememberLocationPermissionState
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.location.Purpose
import com.yandex.mapkit.location.SubscriptionSettings
import com.yandex.mapkit.location.UseInBackground
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.image.ImageProvider

@Composable
fun EditMapScreen(
    state: EditorState,
    onEvent: (EditorEvent) -> Unit,
) {

    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val map = mapView.mapWindow.map

    val onEventState by rememberUpdatedState(onEvent)

    //Состояние карты
    var cameraLat by rememberSaveable { mutableDoubleStateOf(55.753089) }
    var cameraLon by rememberSaveable { mutableDoubleStateOf(37.622651) }
    var cameraZoom by rememberSaveable { mutableFloatStateOf(10f) }
    var initialLocationSet by rememberSaveable { mutableStateOf(false) }
    val defaultZoom = 15f
    fun setLocation(point: Point){
        cameraLat = point.latitude
        cameraLon = point.longitude
        cameraZoom = defaultZoom
        initialLocationSet = true
    }

    val locationManager = remember {
        MapKitFactory.getInstance().createLocationManager()
    }
    val permissionState = rememberLocationPermissionState()

    var permissionRequested by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!permissionState.hasPermission && !permissionRequested) {
            permissionRequested = true
            permissionState.requestPermission()
        }
    }

    var userLocation by remember { mutableStateOf<Point?>(null) }

    var userLocationLayer by remember { mutableStateOf<UserLocationLayer?>(null) }

    DisposableEffect(permissionState.hasPermission) {
        if (!permissionState.hasPermission) {
            onDispose { }
        } else {

            userLocationLayer = createUserLocationLayer(mapView)

            userLocationLayer?.apply {
                setDefaultSource()
                isVisible = true
                isAutoZoomEnabled = true
                isHeadingModeActive = false
            }

            val listener = object : LocationListener {
                override fun onLocationUpdated(location: com.yandex.mapkit.location.Location) {
                    val point = location.position
                    userLocation = point

//                    if (!initialLocationSet) {
//                        setLocation(point)
//                    }
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
    }


    DisposableEffect(Unit) {
        val inputListener = object : InputListener {
            override fun onMapTap(map: Map, point: Point) {}

            override fun onMapLongTap(map: Map, point: Point) {
                onEventState(
                    EditorEvent.DraftPointSelected(
                        RoutePointModel(
                            address = "",
                            pointName = "",
                            pointDescription = "",
                            photoUri = null,
                            latitude = point.latitude,
                            longitude = point.longitude
                        )
                    )
                )
            }
        }

        map.addInputListener(inputListener)

        MapKitFactory.getInstance().onStart()
        mapView.onStart()

        onDispose {
            map.removeInputListener(inputListener)
            mapView.onStop()
            MapKitFactory.getInstance().onStop()
        }
    }

    LaunchedEffect(cameraLat, cameraLon, cameraZoom) {
        moveMap(
            map,
            Point(cameraLat, cameraLon),
            cameraZoom,
        )
    }

    LaunchedEffect(state.points, state.draftPoint) {
        if (!initialLocationSet && state.points.isNotEmpty()){
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
            addMarker(
                mapObjects = mapObjects,
                point = Point(point.latitude, point.longitude),
                context = context,
                isSaved = true,
                isLast = index == state.points.lastIndex
            )
        }

        state.draftPoint?.let { draft ->
            addMarker(
                mapObjects = mapObjects,
                point = Point(draft.latitude, draft.longitude),
                context = context,
                isSaved = false,
                isLast = false
            )
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { mapView })
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp)
                .padding(bottom = 16.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        )
        {
            Column(
                verticalArrangement =  Arrangement.Center,
                modifier = Modifier.weight(1.0f).align(Alignment.End),
            ) {
                SmallFloatingActionButton(
                    onClick = {
                        cameraZoom = (cameraZoom + 1f).coerceAtMost(19f)
                        val target = map.cameraPosition.target
                        cameraLat = target.latitude
                        cameraLon = target.longitude
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Приблизить")
                }
                Spacer(modifier = Modifier.height(12.dp))
                SmallFloatingActionButton(
                    onClick = {
                        cameraZoom = (cameraZoom - 1f).coerceAtLeast(3f)
                        val target = map.cameraPosition.target
                        cameraLat = target.latitude
                        cameraLon = target.longitude

                    }
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Отдалить")
                }


            }
            Row(
                modifier = Modifier.align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                if (state.draftPoint != null) {
                    FloatingActionButton(
                        onClick = { onEvent(EditorEvent.ConfirmMapPoint) }
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Подтвердить")
                    }
                }
                // FAB: моя локация
                FloatingActionButton(
                    onClick = {
                        if (permissionState.hasPermission) {
                            userLocation?.let {
                                moveMap(
                                    map,
                                    CameraState(
                                        latitude = it.latitude,
                                        longitude = it.longitude,
                                        zoom = defaultZoom,
                                        azimuth = 0.0f,
                                        tilt = 0.0f
                                    )
                                )
                                cameraLat = it.latitude
                                cameraLon = it.longitude
                                cameraZoom = defaultZoom
                            }
                        } else {
                            permissionState.requestPermission()
                        }
                    }
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    if (userLocation == null && permissionState.hasPermission) {
                        CircularProgressIndicator()
                    }
                }

            }
        }
    }
    // Темная тема
    val isDark = isSystemInDarkTheme()
    LaunchedEffect(isDark) {
        map.isNightModeEnabled = isDark
    }
}

private fun createUserLocationLayer(mapView: MapView): UserLocationLayer {
    return MapKitFactory.getInstance().createUserLocationLayer(mapView.mapWindow)
}

private fun addMarker(
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