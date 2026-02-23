package com.example.vk_android_vkat.features.explore.routeinfo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RouteInfoScreen( //TODO(Реализовать экран для маршрута)
    state: RouteInfoState,
    onEvent: (RouteInfoEvent) -> Unit
    ){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        var text = ""
        when(state) {
            is RouteInfoState.Error -> text = "Error"
            RouteInfoState.Loading -> { text = "Loading"}
            is RouteInfoState.RouteInfoLoaded -> { text = "Success"}
        }
        Text(
            text = "State is $text"
        )
    }
}