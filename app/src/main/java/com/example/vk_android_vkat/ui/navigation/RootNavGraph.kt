package com.example.vk_android_vkat.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.vk_android_vkat.ui.add.EditorScreen
import com.example.vk_android_vkat.ui.explore.ExploreViewModel
import com.example.vk_android_vkat.ui.explore.SearchScreen
import com.example.vk_android_vkat.ui.explore.SearchUiEvent
import com.example.vk_android_vkat.ui.favourite.FavouriteScreen
import com.example.vk_android_vkat.ui.map.MapScreen
import com.example.vk_android_vkat.ui.profile.ProfileItemUi
import com.example.vk_android_vkat.ui.profile.ProfileScreen
import com.example.vk_android_vkat.ui.profile.ProfileSection
import com.example.vk_android_vkat.ui.profile.ProfileUiEvent
import com.example.vk_android_vkat.ui.profile.ProfileViewModel

@Composable
fun RootNavGraph(
    navController: NavHostController,
    modifier : Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    NavHost(
        navController = navController,
        startDestination = Search,
        modifier = modifier.fillMaxSize(),
        ) {

        composable<Search> {
            val viewModel: ExploreViewModel = viewModel()
            val uiState by viewModel.state.collectAsState()

            SearchScreen(
                uiState = uiState,
                onEvent = { event ->
                    viewModel.onEvent(event)
                    when (event) {
                        is SearchUiEvent.RouteClicked -> navController.navigate(RouteDetails(event.routeId))
                        is SearchUiEvent.FilterClicked -> TODO("Логика фильтров")
                        SearchUiEvent.Retry -> TODO()
                    }
                }
            )
        }

        composable<Profile> {
            val viewModel: ProfileViewModel = viewModel()
            val uiState by viewModel.state.collectAsState()

            ProfileScreen(
                uiState = uiState,
                onEvent = { event ->
                    viewModel.onEvent(event)
                    when (event) {
                        is ProfileUiEvent.ItemClicked -> {
                            // Навигация по секциям настроек
                            when (event.id) {
                                is ProfileItemUi.Navigation -> {
                                    val section = event.id.section
                                    when (section) {
                                        ProfileSection.Account -> TODO("Account screen")
                                        ProfileSection.Notifications -> TODO("Notifications screen")
                                        ProfileSection.Privacy -> TODO("Privacy screen")
                                        ProfileSection.Appearance -> TODO("Appearance screen")
                                        ProfileSection.About -> TODO("About screen")
                                    }
                                }

                                else -> {}
                            }
                        }

                        is ProfileUiEvent.SwitchChanged -> {} // handled inside ViewModel
                        ProfileUiEvent.Retry -> {}
                    }
                }
            )
        }

        composable<Map> { backStackEntry ->
            val details = backStackEntry.toRoute<Map>()
            MapScreen()
        }

        composable<Editor> { backStackEntry ->
            val details = backStackEntry.toRoute<Editor>()
            EditorScreen()
        }

        composable<Favourite> { backStackEntry ->
            val details = backStackEntry.toRoute<Favourite>()
            FavouriteScreen()
        }
    }
}
