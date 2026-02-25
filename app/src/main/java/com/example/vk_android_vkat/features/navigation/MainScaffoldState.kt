package com.example.vk_android_vkat.features.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

class MainScaffoldState {
    var topBar: (@Composable () -> Unit)? by mutableStateOf(null)
}

val LocalMainScaffoldState = staticCompositionLocalOf<MainScaffoldState> {
    error("No MainScaffoldState provided")
}