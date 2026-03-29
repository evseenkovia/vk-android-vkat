package com.example.vk_android_vkat.features.editor.map

import android.Manifest
import com.example.vk_android_vkat.R
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
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
import com.example.vk_android_vkat.features.navigation.EditPointScreen

@Composable
fun EditMapScreen(
    state: MapState,
    navController: NavHostController,
    onEvent: (MapEvent) -> Unit
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val map = mapView.mapWindow.map

    val onEventState by rememberUpdatedState(onEvent)

    //Location stuff
    var userLocationLayer by remember { mutableStateOf<UserLocationLayer?>(null) }
    val locationManager = remember {
        MapKitFactory.getInstance().createLocationManager()
    }
    //permissions
    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


    //placemarks
    var lastMarker: PlacemarkMapObject? by remember { mutableStateOf(null) }

    DisposableEffect(hasLocationPermission) {
        Log.d("mymap", "hasLocationPermission")

        if (hasLocationPermission) {
            userLocationLayer = createUserLocationLayer(mapView)
        }

        val listener = object : LocationListener {
            override fun onLocationUpdated(location: com.yandex.mapkit.location.Location) {
                val point = location.position

                Log.d("mymap", "REAL LOCATION: ${point.latitude}, ${point.longitude}")

                onEventState(MapEvent.OnCurrentLocation(point))
            }

            override fun onLocationStatusUpdated(status: LocationStatus) {
                Log.d("mymap", "Location status: $status")
            }
        }
        val settings = SubscriptionSettings(UseInBackground.DISALLOW,Purpose.STATIC_DISPLAY_LOCATION)
        locationManager.subscribeForLocationUpdates(
            settings,
            listener
        )
        onDispose {
            locationManager.unsubscribe(listener)
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
        val inputListener = object : InputListener {
            override fun onMapTap(map: Map, point: Point) {}
            override fun onMapLongTap(map: Map, point: Point) {
                onEventState(MapEvent.LongTap(point))
            }
        }
        map.addInputListener(inputListener)

        MapKitFactory.getInstance().onStart()
        mapView.onStart()

        onDispose {
            onEventState(MapEvent.CameraSave(map.cameraPosition.target, map.cameraPosition.zoom)) //save last pos
            map.removeInputListener(inputListener)
            mapView.onStop()
            MapKitFactory.getInstance().onStop()
        }
    }
    LaunchedEffect(state.cameraPosition, state.zoom) {
        map.move(
            CameraPosition(state.cameraPosition, state.zoom, 0f, 0f)
        )
    }

    LaunchedEffect(state.lastPlacemark) {
        Log.d("mymap", "lastPlacemark change")
        if (state.lastPlacemark != null){
            val mapObjects = map.mapObjects
            lastMarker?.let { mapObjects.remove(it) }
            lastMarker = addMarker(
                mapObjects = map.mapObjects,
                point = state.lastPlacemark,
                context = context,
                isSaved = false,
                isLast = false
            )
        }
    }
    LaunchedEffect(state.placemarks) {
        //Камера на последней точке
        state.placemarks.lastOrNull()?.let { lastPoint ->
            onEvent(MapEvent.OnCurrentLocation(lastPoint))
        }
        val mapObjects = map.mapObjects

        state.placemarks.forEachIndexed { index, point ->
            addMarker(
                mapObjects = mapObjects,
                point = point,
                context = context,
                isSaved = true,
                isLast = index == state.placemarks.lastIndex
            )
        }
        state.lastPlacemark?.let { point ->
            addMarker(
                mapObjects = mapObjects,
                point = point,
                context = context,
                isSaved = false,
                isLast = false
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { mapView })

        if (state.lastPlacemark != null) {
            FloatingActionButton(
                onClick = {
                    onEventState(MapEvent.ConfirmPlacemark(map.cameraPosition.zoom.toInt()))
                    navController.navigate(EditPointScreen)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .padding(bottom = 16.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Подтвердить")
            }
        }
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