
package com.example.vk_android_vkat.features.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.createBitmap
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
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.ln
import kotlin.math.log2
import kotlin.math.min
import kotlin.math.sinh

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
        MapView(context)
    }
    val map = mapView.mapWindow.map

    var cameraLat by rememberSaveable { mutableDoubleStateOf(55.753089) }
    var cameraLon by rememberSaveable { mutableDoubleStateOf(37.622651) }
    var cameraZoom by rememberSaveable { mutableFloatStateOf(10f) }
    var cameraAzimuth by rememberSaveable { mutableFloatStateOf(0f) }
    var cameraTilt by rememberSaveable { mutableFloatStateOf(0f) }

    var initialLocationSet by rememberSaveable { mutableStateOf(false) }
    val defaultZoom = 15f

    var cameraState = CameraState(
        latitude = cameraLat,
        longitude = cameraLon,
        zoom = cameraZoom,
        azimuth = cameraAzimuth,
        tilt = cameraTilt
    )

    fun updateCameraState(newState: CameraState) {
        cameraLat = newState.latitude
        cameraLon = newState.longitude
        cameraZoom = newState.zoom
        cameraAzimuth = newState.azimuth
        cameraTilt = newState.tilt
        cameraState = CameraState(
            latitude = cameraLat,
            longitude = cameraLon,
            zoom = cameraZoom,
            azimuth = cameraAzimuth,
            tilt = cameraTilt
        )
    }

    fun setLocation(point: Point) {
        updateCameraState(
            cameraState.copy(
                latitude = point.latitude,
                longitude = point.longitude,
                zoom = defaultZoom,
                azimuth = 0f,
                tilt = 0f
            )
        )
        initialLocationSet = true
    }

    LaunchedEffect(cameraState) {
        moveMap(map, cameraState)
    }

    var userLocation by remember { mutableStateOf<Point?>(null) }
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

    val userLocationLayer = remember {
        MapKitFactory.getInstance()
            .createUserLocationLayer(mapView.mapWindow)
            .apply { isVisible = false }
    }

    DisposableEffect(permissionState.hasPermission) {
        if (!permissionState.hasPermission) {
            userLocationLayer.isVisible = false
            onDispose { }
        } else {
            userLocationLayer.setDefaultSource()
            userLocationLayer.isVisible = true
            userLocationLayer.isAutoZoomEnabled = true
            userLocationLayer.isHeadingModeActive = false

            val listener = object : LocationListener {
                override fun onLocationUpdated(location: com.yandex.mapkit.location.Location) {
                    val point = location.position
                    userLocation = point
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
                    updateCameraState(
                        CameraState(
                            latitude = current.target.latitude,
                            longitude = current.target.longitude,
                            zoom = current.zoom,
                            azimuth = current.azimuth,
                            tilt = current.tilt
                        )
                    )
                }

                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val clusterBgColor = MaterialTheme.colorScheme.primary.toArgb()
    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val clusterStrokeColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val haloColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f).toArgb()

    val currentCameraState by rememberUpdatedState(cameraState)

    val clusterTapListener = remember(mapView) {
        ClusterTapListener { cluster ->
            zoomToCluster(
                mapView = mapView,
                cameraState = currentCameraState,
                onCameraStateChange = ::updateCameraState,
                cluster = cluster,
                paddingPx = 96 * 3,
                minZoom = 3f,
                maxZoom = 19f
            )
            true
        }
    }

    val clusterListener = remember(context, clusterBgColor, textColor) {
        ClusterListener { cluster ->
            cluster.appearance.setIcon(
                ClusterCountImageProvider(
                    count = cluster.size,
                    backgroundColor = clusterBgColor,
                    textColor = textColor,
                    context = context,
                    strokeColor = clusterStrokeColor,
                    strokeWidthDp = 4.0f
                )
            )
            cluster.addClusterTapListener(clusterTapListener)
        }
    }

    val routeClusterCollection = remember(map) {
        map.mapObjects.addClusterizedPlacemarkCollection(clusterListener)
    }

    val tapListeners = remember { mutableListOf<MapObjectTapListener>() }
    DisposableEffect(state.markers) {
        Log.d("MapScreen", "Updating markers: ${state.markers.size}")

        routeClusterCollection.clear()

        state.markers.forEach { marker ->
            val routeId = marker.id

            val listener = MapObjectTapListener { _, _ ->
                Log.d("MapScreen", "Marker tapped: ${marker.title}")
                onRouteClick(routeId)
                true
            }

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
                        color = textColor
                        placement = TextStyle.Placement.BOTTOM
                        offset = 8f
                        outlineWidth = 12f
                        outlineColor = haloColor
                    }
                )

                addTapListener(listener)
                tapListeners.add(listener)
            }
        }

        routeClusterCollection.clusterPlacemarks(CLUSTER_RADIUS, CLUSTER_MIN_ZOOM)

        onDispose {
            routeClusterCollection.clear()
            tapListeners.clear()
        }
    }

    val isDark = isSystemInDarkTheme()
    LaunchedEffect(isDark) {
        map.isNightModeEnabled = isDark
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp)
                .padding(bottom = 16.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ){
            Column(
                verticalArrangement =  Arrangement.Center,
                modifier = Modifier.weight(1.0f).align(Alignment.End),
            ) {
                SmallFloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.secondary,
                    onClick = {
                        updateCameraState(
                            cameraState.copy(
                                zoom = (cameraState.zoom + 1f).coerceAtMost(19f),
                                latitude = map.cameraPosition.target.latitude,
                                longitude = map.cameraPosition.target.longitude
                            )
                        )
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Приблизить")
                }
                Spacer(modifier = Modifier.height(12.dp))
                SmallFloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.secondary,
                    onClick = {
                        updateCameraState(
                            cameraState.copy(
                                zoom = (cameraState.zoom - 1f).coerceAtLeast(3f),
                                latitude = map.cameraPosition.target.latitude,
                                longitude = map.cameraPosition.target.longitude
                            )
                        )
                    }
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Отдалить")
                }
            }
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.secondary,
                onClick = {
                    if (permissionState.hasPermission) {
                        userLocation?.let {
                            moveMap(
                                map,
                                CameraState(
                                    latitude = it.latitude,
                                    longitude = it.longitude,
                                    zoom = defaultZoom,
                                    azimuth = cameraState.azimuth,
                                    tilt = cameraState.tilt
                                )
                            )
                            updateCameraState(
                                CameraState(
                                    latitude = it.latitude,
                                    longitude = it.longitude,
                                    zoom = defaultZoom,
                                    azimuth = cameraState.azimuth,
                                    tilt = cameraState.tilt
                                )
                            )
                        } ?: permissionState.requestPermission()
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

private fun zoomToCluster(
    mapView: MapView,
    cameraState: CameraState,
    onCameraStateChange: (CameraState) -> Unit,
    cluster: Cluster,
    paddingPx: Int = 96,
    minZoom: Float = 3f,
    maxZoom: Float = 19f
) {
    val points = cluster.placemarks.map { it.geometry }
    if (points.isEmpty()) return

    val minLat = points.minOf { it.latitude }
    val maxLat = points.maxOf { it.latitude }
    val minLon = points.minOf { it.longitude }
    val maxLon = points.maxOf { it.longitude }

    val width = mapView.width
    val height = mapView.height

    val squareSide = if (width > 0 && height > 0) {
        (minOf(width, height) - 2 * paddingPx).coerceAtLeast(1)
    } else {
        1
    }

    val zoom = calculateZoomForBounds(
        minLat = minLat,
        maxLat = maxLat,
        minLon = minLon,
        maxLon = maxLon,
        viewWidthPx = squareSide,
        viewHeightPx = squareSide,
        minZoom = minZoom,
        maxZoom = maxZoom
    )

    val center = mercatorCenter(
        minLat = minLat,
        maxLat = maxLat,
        minLon = minLon,
        maxLon = maxLon
    )

    onCameraStateChange(
        cameraState.copy(
            latitude = center.latitude,
            longitude = center.longitude,
            zoom = zoom
        )
    )
}

private fun calculateZoomForBounds(
    minLat: Double,
    maxLat: Double,
    minLon: Double,
    maxLon: Double,
    viewWidthPx: Int,
    viewHeightPx: Int,
    minZoom: Float,
    maxZoom: Float
): Float {
    val (x1, y1) = latLonToWorld(minLat, minLon)
    val (x2, y2) = latLonToWorld(maxLat, maxLon)

    val dx = abs(x2 - x1).coerceAtLeast(1e-9)
    val dy = abs(y2 - y1).coerceAtLeast(1e-9)

    val zoomX = log2(viewWidthPx / (256.0 * dx))
    val zoomY = log2(viewHeightPx / (256.0 * dy))

    return min(zoomX, zoomY).toFloat().coerceIn(minZoom, maxZoom)
}

private fun latLonToWorld(lat: Double, lon: Double): Pair<Double, Double> {
    val x = (lon + 180.0) / 360.0
    val sinLat = kotlin.math.sin(Math.toRadians(lat)).coerceIn(-0.9999, 0.9999)
    val y = 0.5 - ln((1.0 + sinLat) / (1.0 - sinLat)) / (4.0 * PI)
    return x to y
}

private fun worldToLatLon(x: Double, y: Double): Point {
    val lon = x * 360.0 - 180.0
    val n = PI - 2.0 * PI * y
    val lat = Math.toDegrees(atan(sinh(n)))
    return Point(lat, lon)
}

private fun mercatorCenter(
    minLat: Double,
    maxLat: Double,
    minLon: Double,
    maxLon: Double
): Point {
    val (x1, y1) = latLonToWorld(minLat, minLon)
    val (x2, y2) = latLonToWorld(maxLat, maxLon)

    val centerX = (x1 + x2) / 2.0
    val centerY = (y1 + y2) / 2.0

    return worldToLatLon(centerX, centerY)
}

fun moveMap(
    map: com.yandex.mapkit.map.Map,
    cameraState: CameraState,
    animationType: Animation.Type = Animation.Type.LINEAR,
    animationDuration: Float = 0.3f
) {
    map.move(
        CameraPosition(
            cameraState.target,
            cameraState.zoom,
            cameraState.azimuth,
            cameraState.tilt
        ),
        Animation(animationType, animationDuration)
    )
}

fun moveMap(
    map: com.yandex.mapkit.map.Map,
    target: com.yandex.mapkit.geometry.Point,
    zoom: Float,
    azimuth: Float = 0f,
    tilt: Float = 0f,
    animationType: Animation.Type = Animation.Type.LINEAR,
    animationDuration: Float = 0.3f
) {
    map.move(
        CameraPosition(
            target,
            zoom,
            azimuth,
            tilt
        ),
        Animation(animationType, animationDuration)
    )
}


private class ClusterCountImageProvider(
    private val count: Int,
    private val backgroundColor: Int,
    private val textColor: Int,
    private val context: Context,
    private val strokeColor: Int = Color.WHITE,
    private val strokeWidthDp: Float = 2f
) : ImageProvider() {

    override fun getId(): String = "cluster_$count"

    override fun getImage(): Bitmap {
        val density = context.resources.displayMetrics.density
        val sizePx = (44f * density).toInt().coerceAtLeast(1)
        val bitmap = createBitmap(sizePx, sizePx)
        val canvas = Canvas(bitmap)

        val radius = sizePx / 2f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }

        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = strokeColor
            style = Paint.Style.STROKE
            strokeWidth = strokeWidthDp * density
        }

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = textColor
            textAlign = Paint.Align.CENTER
            textSize = 24f * density
            style = Paint.Style.FILL
        }

        canvas.drawCircle(radius, radius, radius, bgPaint)
        canvas.drawCircle(
            radius,
            radius,
            radius - strokePaint.strokeWidth / 2f,
            strokePaint
        )

        val fm = textPaint.fontMetrics
        val textY = radius - (fm.ascent + fm.descent) / 2f
        canvas.drawText(count.toString(), radius, textY, textPaint)

        return bitmap
    }
}
