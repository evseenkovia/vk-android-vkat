package com.example.vk_android_vkat.features.explore.routeinfo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RouteInfoScreen(
    state: RouteInfoState? = null,
    onEvent: (RouteInfoEvent) -> Unit
    ){

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(state is RouteInfoState.RouteInfoLoaded)
        Text(
            text = "This is RouteInfoScreen @Composable with an id = ${state.route.id}"
        )
    }
}