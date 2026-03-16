package com.example.vk_android_vkat.features.favourite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.vk_android_vkat.features.explore.ui.ExploreEvent
import com.example.vk_android_vkat.features.explore.ui.ExploreScreen
import com.example.vk_android_vkat.features.explore.ui.ExploreState

@Composable
fun FavouriteScreen(
    state: ExploreState,
    onEvent: (ExploreEvent) -> Unit,
    onRouteClick: (Long) -> Unit = {},
) {

    val favouriteState = when (state) {

        is ExploreState.Routes -> {
            val favouriteRoutes = remember (state.data) {state.data.filter { it.isFavourite }}
            ExploreState.Routes(favouriteRoutes)
        }

        else -> state
    }

    ExploreScreen(
        state = favouriteState,
        onEvent = onEvent,
        onRouteClick = onRouteClick
    )
}