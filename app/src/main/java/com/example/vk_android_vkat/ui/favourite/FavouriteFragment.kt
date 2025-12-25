package com.example.vk_android_vkat.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.ui.explore.ExploreViewModel
import com.example.vk_android_vkat.ui.explore.ExploreViewModelFactory
import com.example.vk_android_vkat.ui.explore.RouteCard

class FavouriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Используем тот же ExploreViewModel
        val viewModel = ViewModelProvider(
            requireActivity(),
            ExploreViewModelFactory(FavouriteDataStore(requireContext()))
        )[ExploreViewModel::class.java]

        return ComposeView(requireContext()).apply {
            setContent {
                FavouriteScreen(viewModel)
            }
        }
    }
}

@Composable
fun FavouriteScreen(viewModel: ExploreViewModel) {

    val routes by viewModel.routes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val favouriteRoutes = routes
        ?.filter { it.isFavourite }
        .orEmpty()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error ?: "",
                        color = Color.Red
                    )
                }
            }

            favouriteRoutes.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.no_favourites))
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 56.dp)
                ) {
                    items(
                        items = favouriteRoutes,
                        key = { it.id }
                    ) { route ->
                        RouteCard(
                            route = route,
                            onFavouriteClick = {
                                viewModel.toggleFavourite(route.id)
                            }
                        )
                    }
                }
            }
        }
    }
}
