package com.example.vk_android_vkat.route

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.vk_android_vkat.ui.explore.ExploreViewModel
import com.example.vk_android_vkat.ui.explore.ExploreViewModelFactory
import com.example.vk_android_vkat.ui.favourite.FavouriteDataStore

class RouteDetailFragment : Fragment() {

    private lateinit var viewModel: ExploreViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val routeId = arguments?.getLong("routeId") ?: 0L

        viewModel = ViewModelProvider(
            this,
            ExploreViewModelFactory(FavouriteDataStore(requireContext()))
        )[ExploreViewModel::class.java]

        return ComposeView(requireContext()).apply {
            setContent {
                val routes by viewModel.routes.collectAsState()
                val route = routes?.find { it.id == routeId }

                route?.let {
                    RouteDetailScreen(route = it, onBack = { requireActivity().onBackPressedDispatcher.onBackPressed() })
                }
            }
        }
    }
}

