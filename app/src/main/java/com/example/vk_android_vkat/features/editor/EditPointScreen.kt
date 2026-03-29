package com.example.vk_android_vkat.features.editor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView


@Composable
fun AddPointScreen(){
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val map = mapView.mapWindow.map
    Scaffold { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            AndroidView(factory = { mapView })
        }
    }
    DisposableEffect(Unit) {
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
        onDispose {
            mapView.onStop()
            MapKitFactory.getInstance().onStop()
        }
    }
}