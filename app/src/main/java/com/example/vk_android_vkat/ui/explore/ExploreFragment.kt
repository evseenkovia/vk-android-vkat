package com.example.vk_android_vkat.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.ui.favourite.FavouriteDataStore

class ExploreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val exploreViewModel =
            ViewModelProvider(this,
                ExploreViewModelFactory(FavouriteDataStore(requireContext()))
            )[ExploreViewModel::class.java]

        return ComposeView(requireContext()).apply {
            setContent {
                ExploreScreen(exploreViewModel)
            }
        }
    }

    @Composable
    fun ExploreScreen(viewModel: ExploreViewModel) {
        val routes by viewModel.routes.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val error by viewModel.error.collectAsState()

        Scaffold(
            topBar = { ToolBar() }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    error != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = error ?: "", color = Color.Red)
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = { viewModel.loadRoutes() }) {
                                    Text(stringResource(R.string.try_again))
                                }
                            }
                        }
                    }
                    routes.isNullOrEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(R.string.no_routes))
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 56.dp)
                        ) {
                            items(routes!!, key = { it.id }) { route ->
                                RouteCard(route = route, onClick = { /* переход */ },
                                    onFavouriteClick = {
                                        viewModel.toggleFavourite(route.id)
                                    })
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun ToolBar() {
        LazyRow(
            modifier = Modifier.
            padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                AssistChip(
                    onClick = { },
                    label = { Text(stringResource(R.string.nearby)) },
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                    }
                )
            }
            item { AssistChip(onClick = { }, label = { Text(stringResource(R.string.target)) }) }
            item { AssistChip(onClick = { }, label = { Text(stringResource(R.string.location)) }) }
            item { AssistChip(onClick = { }, label = { Text(stringResource(R.string.day_time)) }) }
            item { AssistChip(onClick = { }, label = { Text(stringResource(R.string.duration)) }) }
        }
    }
}