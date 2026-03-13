package com.example.vk_android_vkat.features.favourite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.vk_android_vkat.features.explore.ExploreEvent
import com.example.vk_android_vkat.features.explore.ExploreScreen
import com.example.vk_android_vkat.features.explore.ExploreState

@Composable
fun FavouriteScreen(
    state: ExploreState,
    onEvent: (ExploreEvent) -> Unit,
    onRouteClick: (Long) -> Unit = {},
) {

    val favouriteState = when (state) {

        is ExploreState.Routes -> {
            val favouriteRoutes = state.data.filter { it.isFavourite }
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