package com.example.vk_android_vkat.features.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.vk_android_vkat.R
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
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

@Composable
fun MapScreen(
    state: MapState,
    onRouteClick: (Int) -> Unit
) {
    val context = LocalContext.current

    //Переменные
    val mapView = remember {
        Log.d("MapScreen", "Creating new MapView")
        MapView(context.applicationContext)
    }
    val map = mapView.mapWindow.map

    val userLocationLayer = remember {
        Log.d("MapScreen", "Creating UserLocationLayer")
        MapKitFactory.getInstance().createUserLocationLayer(mapView.mapWindow)
    }

    LaunchedEffect(userLocationLayer) {
        userLocationLayer.apply {
            setDefaultSource()
            isVisible = true
            isAutoZoomEnabled = true
            isHeadingModeActive = false
        }
    }

    //Камера + локация
    var cameraLat by rememberSaveable { mutableDoubleStateOf(55.753089) }
    var cameraLng by rememberSaveable { mutableDoubleStateOf(37.622651) }
    var cameraZoom by rememberSaveable { mutableFloatStateOf(10f) }
    var initialLocationSet by rememberSaveable { mutableStateOf(false) }
    val defaultZoom = 15f

    fun setLocation(point: Point) {
        cameraLat = point.latitude
        cameraLng = point.longitude
        cameraZoom = defaultZoom
        initialLocationSet = true
    }

    val locationManager = remember {
        MapKitFactory.getInstance().createLocationManager()
    }
    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    //Оновление локации
    DisposableEffect(hasLocationPermission) {
        if (!hasLocationPermission) return@DisposableEffect onDispose {}

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
        }
    }

    //lifecycle mapView
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    Log.d("MapScreen", "ON_START: MapKit")
                    mapView.onStart()
                }
                Lifecycle.Event.ON_STOP -> {
                    Log.d("MapScreen", "ON_STOP: MapView")
                    mapView.onStop()
                    //Сохранение текущего положения
                    val current = map.cameraPosition
                    cameraLat = current.target.latitude
                    cameraLng = current.target.longitude
                    cameraZoom = current.zoom
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    //Обновление координат
    LaunchedEffect(cameraLat, cameraLng, cameraZoom) {
        map.move(
            CameraPosition(
                Point(cameraLat, cameraLng),
                cameraZoom,
                0f,
                0f
            )
        )
    }

    val haloColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f).toArgb()
    val tapListeners = remember { mutableStateListOf<MapObjectTapListener>() }
    //Обновление Маркеров
    LaunchedEffect(state.markers) {
        Log.d("MapScreen", "Updating markers: ${state.markers.size}")
        val mapObjects = map.mapObjects
        mapObjects.clear()
        tapListeners.clear()

        state.markers.forEach { marker ->
            val routeId = marker.id
            addRouteStartPlacemark(
                mapObjects = mapObjects,
                point = Point(marker.lat, marker.lng),
                title = marker.title,
                context = context,
                textHaloColor = haloColor,
                onTap = { _, _ ->
                    Log.d("MapScreen", "Marker tapped: ${marker.title}")
                    onRouteClick(routeId)
                },
                tapListeners = tapListeners
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )
    }
}

private fun addRouteStartPlacemark(
    mapObjects: MapObjectCollection,
    point: Point,
    title: String,
    context: Context,
    textHaloColor: Int,
    onTap: (Point, String) -> Unit,
    tapListeners: MutableList<MapObjectTapListener>
) {
    val placemark = mapObjects.addPlacemark().apply {
        geometry = point
        setIcon(
            ImageProvider.fromResource(context, R.drawable.location),
            IconStyle().apply {
                anchor = PointF(0.5f, 1.0f)
                scale = 0.4f
                flat = false
            }
        )
        setText(
            title,
            TextStyle().apply {
                size = 14f
                color = ContextCompat.getColor(context, android.R.color.black)
                placement = TextStyle.Placement.BOTTOM
                offset = 8f
                outlineWidth = 12f
                outlineColor = textHaloColor
            }
        )

        val listener = MapObjectTapListener { _, tappedPoint ->
            onTap(tappedPoint, title)
            true
        }
        tapListeners.add(listener)
        addTapListener(listener)
    }
}