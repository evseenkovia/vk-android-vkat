package com.example.vk_android_vkat.features.favourite.ui

import androidx.compose.runtime.Composable
import com.example.vk_android_vkat.features.explore.ui.ExploreEvent
import com.example.vk_android_vkat.features.explore.ui.ExploreScreen
import com.example.vk_android_vkat.features.explore.ui.ExploreState

@Composable
fun FavouriteScreen(
    state: ExploreState,
    onEvent: (ExploreEvent) -> Unit,
    onRouteClick: (Int) -> Unit = {},
    onEnter: () -> Unit
) {
    // Меняем состояние (показываем только Избранные)
    onEnter.invoke()

    ExploreScreen(
        state = state,
        onEvent = onEvent,
        onRouteClick = onRouteClick
    )
}