package com.example.vk_android_vkat.features.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.vk_android_vkat.features.editor.EditorScreen
import com.example.vk_android_vkat.features.explore.ExploreScreen
import com.example.vk_android_vkat.features.explore.ExploreViewModel
import com.example.vk_android_vkat.features.explore.data.RouteRepositoryMock
import com.example.vk_android_vkat.features.explore.routeinfo.ui.RouteInfoEffect
import com.example.vk_android_vkat.features.explore.routeinfo.ui.RouteInfoScreen
import com.example.vk_android_vkat.features.explore.routeinfo.ui.RouteInfoViewModel
import com.example.vk_android_vkat.features.favourite.FavouriteScreen
import com.example.vk_android_vkat.features.map.MapScreen
import com.example.vk_android_vkat.features.profile.ProfileItemUi
import com.example.vk_android_vkat.features.profile.ProfileScreen
import com.example.vk_android_vkat.features.profile.ProfileSection
import com.example.vk_android_vkat.features.profile.ProfileUiEvent
import com.example.vk_android_vkat.features.profile.ProfileViewModel

import kotlinx.serialization.Serializable

//------ Графы навигации для табов ------
@Serializable
object ExploreGraph
@Serializable
object FavouriteGraph
@Serializable
object EditorGraph
@Serializable
object MapGraph
@Serializable
object ProfileGraph

//------ Основные графы (авторизация и главный экран) ------
@Serializable
object AuthGraph
@Serializable
object MainGraph


//------ Основные экраны ------
@Serializable
object Explore
@Serializable
object Favourite
@Serializable
object Editor
@Serializable
object Map
@Serializable
object Profile

@Serializable
object Settings

//------ Экраны авторизации ------
@Serializable
object Login
@Serializable
object Registration
@Serializable
object PasswordRecovery

//------ Экраны деталей ------
@Serializable
data class RouteInfo(val routeId: Long)

//------ Расширения для графов ------

fun NavGraphBuilder.exploreGraph(navController : NavHostController){

    navigation<ExploreGraph>(startDestination = Explore) {

        composable<Explore> {
            val viewModel: ExploreViewModel = viewModel()
            val uiState by viewModel.state.collectAsState()

            ExploreScreen(
                state = uiState,
                onEvent = viewModel::onEvent,
                onRouteClick = { routeId: Long ->
                    navController.navigate(RouteInfo(routeId))
                }
            )
        }

        composable<RouteInfo>() { backStackEntry ->

            val routeId = backStackEntry.toRoute<RouteInfo>().routeId
            val viewModel = remember {
                RouteInfoViewModel(routeId, RouteRepositoryMock())
            }

            val effect by viewModel.effect.collectAsState(initial = null)
            LaunchedEffect(effect) {
                when(effect) {
                    RouteInfoEffect.NavigateBack -> navController.popBackStack()
                    else -> {}
                }
            }

            val state by viewModel.state.collectAsState()
            RouteInfoScreen(
                state = state,
                onEvent = viewModel::onEvent,
            )
        }
    }
}

fun NavGraphBuilder.favouriteGraph(navController: NavHostController) {
    navigation<FavouriteGraph>(startDestination = Favourite){

        composable<Favourite> { backStackEntry ->
            val details = backStackEntry.toRoute<Favourite>()
            FavouriteScreen()
        }
    }
}

fun NavGraphBuilder.editorGraph(navController: NavHostController) {
    navigation<EditorGraph>(startDestination = Editor){

        composable<Editor> { backStackEntry ->
            val details = backStackEntry.toRoute<Editor>()
            EditorScreen()
        }
    }
}

fun NavGraphBuilder.mapGraph(navController: NavHostController) {
    navigation<MapGraph>(startDestination = Map){

        composable<Map> { backStackEntry ->
            val details = backStackEntry.toRoute<Map>()
            MapScreen()
        }
    }
}

fun NavGraphBuilder.profileGraph(navController: NavHostController) {
    navigation<ProfileGraph>(startDestination = Profile){
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

                        is ProfileUiEvent.SwitchChanged -> {}
                        ProfileUiEvent.Retry -> {}
                    }
                }
            )
        }
    }
}
