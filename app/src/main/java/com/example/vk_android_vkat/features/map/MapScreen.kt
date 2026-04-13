package com.example.vk_android_vkat.features.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.location.Purpose
import com.yandex.mapkit.location.SubscriptionSettings
import com.yandex.mapkit.location.UseInBackground
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Cluster
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.ClusterTapListener
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

private const val CLUSTER_RADIUS = 60.0
private const val CLUSTER_MIN_ZOOM = 15

@Composable
fun MapScreen(
    state: MapState,
    onRouteClick: (Int) -> Unit
) {
    val context = LocalContext.current

    val mapView = remember {
        Log.d("MapScreen", "Creating new MapView")
        MapView(context.applicationContext)
    }
    val map = mapView.mapWindow.map
    val tapListeners = remember { mutableListOf<MapObjectTapListener>() }
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

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    Log.d("MapScreen", "ON_START: MapView")
                    mapView.onStart()
                }
                Lifecycle.Event.ON_STOP -> {
                    Log.d("MapScreen", "ON_STOP: MapView")
                    mapView.onStop()

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

    val clusterTapListener = remember {
        ClusterTapListener { cluster ->
            zoomToCluster(map, cluster)
            true
        }
    }

    val clusterBgColor = MaterialTheme.colorScheme.secondary.toArgb()
    val clusterTextColor = MaterialTheme.colorScheme.onSecondary.toArgb()

    val clusterListener = remember(context, clusterBgColor, clusterTextColor) {
        ClusterListener { cluster ->
            cluster.appearance.setIcon(
                ClusterCountImageProvider(
                    count = cluster.size,
                    backgroundColor = clusterBgColor,
                    textColor = clusterTextColor,
                    context = context
                )
            )
            cluster.addClusterTapListener(clusterTapListener)
        }
    }

    val routeClusterCollection = remember(map) {
        map.mapObjects.addClusterizedPlacemarkCollection(clusterListener)
    }
    LaunchedEffect(state.markers) {
        Log.d("MapScreen", "Updating markers: ${state.markers.size}")

        routeClusterCollection.clear()
        tapListeners.clear()

        state.markers.forEach { marker ->
            val routeId = marker.id

            val listener = MapObjectTapListener { _, _ ->
                Log.d("MapScreen", "Marker tapped: ${marker.title}")
                onRouteClick(routeId)
                true
            }

            tapListeners.add(listener)

            routeClusterCollection.addPlacemark().apply {
                geometry = Point(marker.lat, marker.lng)

                setIcon(
                    ImageProvider.fromResource(context, R.drawable.location),
                    IconStyle().apply {
                        anchor = PointF(0.5f, 1.0f)
                        scale = 0.4f
                        flat = false
                    }
                )

                setText(
                    marker.title,
                    TextStyle().apply {
                        size = 14f
                        color = ContextCompat.getColor(context, android.R.color.black)
                        placement = TextStyle.Placement.BOTTOM
                        offset = 8f
                        outlineWidth = 12f
                        outlineColor = haloColor
                    }
                )

                addTapListener(listener)
            }
        }

        routeClusterCollection.clusterPlacemarks(CLUSTER_RADIUS, CLUSTER_MIN_ZOOM)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )
    }
}
private fun zoomToCluster(
    map: com.yandex.mapkit.map.Map,
    cluster: Cluster,
    zoomOffset: Float = 1.5f,
    maxZoom: Float = 19f
) {
    val points = cluster.placemarks.map { it.geometry }
    if (points.isEmpty()) return

    val (minLat, maxLat) = points.minOf { it.latitude } to points.maxOf { it.latitude }
    val (minLon, maxLon) = points.minOf { it.longitude } to points.maxOf { it.longitude }

    val center = Point(
        (minLat + maxLat) / 2.0,
        (minLon + maxLon) / 2.0
    )

    val newZoom = (map.cameraPosition.zoom + zoomOffset).coerceAtMost(maxZoom)

    moveMap(map, center, newZoom)
}

private fun moveMap(
    map: com.yandex.mapkit.map.Map,
    target: com.yandex.mapkit.geometry.Point,
    zoom: Float,
    azimuth: Float = 0f,
    tilt: Float = 0f,
    animationType: Animation.Type = Animation.Type.SMOOTH,
    animationDuration: Float = 0.5f
) {
    map.move(
        com.yandex.mapkit.map.CameraPosition(target, zoom, azimuth, tilt),
        Animation(animationType, animationDuration)
    )
}


private fun moveMap(
    map: com.yandex.mapkit.map.Map,
    target: com.yandex.mapkit.geometry.Point,
    zoom: Float,
    azimuth: Float = 0f,
    tilt: Float = 0f,
    animation: Animation? = Animation(
        Animation.Type.SMOOTH,
        0.5f
    )
) {
    val cameraPosition = CameraPosition(target, zoom, azimuth, tilt)
    if (animation != null) {
        map.move(cameraPosition, animation)
    } else {
        map.move(cameraPosition)
    }
}
private class ClusterCountImageProvider(
    private val count: Int,
    private val backgroundColor: Int,
    private val textColor: Int,
    private val context: Context
) : ImageProvider() {

    override fun getId(): String = "cluster_$count"

    override fun getImage(): Bitmap {
        val density = context.resources.displayMetrics.density

        val sizePx = (44f * density).toInt().coerceAtLeast(1)
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = textColor
            textAlign = Paint.Align.CENTER
            textSize = 14f * density
            style = Paint.Style.FILL
        }

        val radius = sizePx / 2f
        canvas.drawCircle(radius, radius, radius, bgPaint)

        val fm = textPaint.fontMetrics
        val textY = radius - (fm.ascent + fm.descent) / 2f
        canvas.drawText(count.toString(), radius, textY, textPaint)

        return bitmap
    }
}