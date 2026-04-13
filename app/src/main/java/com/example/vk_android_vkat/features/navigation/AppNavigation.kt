package com.example.vk_android_vkat.features.navigation

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.vk_android_vkat.features.editor.EditPointScreen
import com.example.vk_android_vkat.features.editor.EditorEffect
import com.example.vk_android_vkat.features.editor.EditorScreen
import com.example.vk_android_vkat.features.editor.EditorViewModel
import com.example.vk_android_vkat.features.editor.map.EditMapScreen
import com.example.vk_android_vkat.features.explore.routeinfo.ui.RouteInfoEffect
import com.example.vk_android_vkat.features.explore.routeinfo.ui.RouteInfoScreen
import com.example.vk_android_vkat.features.explore.routeinfo.ui.RouteInfoViewModel
import com.example.vk_android_vkat.features.explore.ui.ExploreEvent
import com.example.vk_android_vkat.features.explore.ui.ExploreScreen
import com.example.vk_android_vkat.features.explore.ui.ExploreViewModel
import com.example.vk_android_vkat.features.favourite.ui.FavouriteScreen
import com.example.vk_android_vkat.features.map.MapScreen
import com.example.vk_android_vkat.features.map.MapViewModel
import com.example.vk_android_vkat.features.profile.ProfileItemUi
import com.example.vk_android_vkat.features.profile.ProfileScreen
import com.example.vk_android_vkat.features.profile.ProfileSection
import com.example.vk_android_vkat.features.profile.ProfileUiEvent
import com.example.vk_android_vkat.features.profile.ProfileViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

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
data class RouteInfo(val routeId: Int)
@Serializable
object EditMapScreen

@Serializable
object EditPointScreen

//------ Расширения для графов ------

fun NavGraphBuilder.exploreGraph(navController : NavHostController){

    navigation<ExploreGraph>(startDestination = Explore) {

        composable<Explore> {
            val viewModel: ExploreViewModel = koinViewModel()
            val uiState by viewModel.state.collectAsStateWithLifecycle()

            // Обновляем экран при изменении состояния
            LaunchedEffect(viewModel.state) {
                viewModel.loadRoutes()
            }

            ExploreScreen(
                state = uiState,
                onEvent = viewModel::onEvent,
                onRouteClick = { routeId: Int ->
                    navController.navigate(RouteInfo(routeId))
                }
            )
        }

        composable<RouteInfo>() { backStackEntry ->

            val routeId = backStackEntry.toRoute<RouteInfo>().routeId

            val viewModel : RouteInfoViewModel = koinViewModel(
                parameters = { parametersOf(routeId) }
            )

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
                onEvent = viewModel::onEvent
            )
        }
    }
}

fun NavGraphBuilder.favouriteGraph(navController: NavHostController) {
    navigation<FavouriteGraph>(startDestination = Favourite){

        composable<Favourite> {
            val viewModel: ExploreViewModel = koinViewModel()
            val uiState by viewModel.state.collectAsStateWithLifecycle()

            FavouriteScreen(
                state = uiState,
                onEvent = viewModel::onEvent,
                onRouteClick = { routeId ->
                    navController.navigate(RouteInfo(routeId))
                },
                onEnter = { viewModel.onEvent(ExploreEvent.ShowFavourites) }
            )
        }
    }
}

fun NavGraphBuilder.editorGraph(navController: NavHostController) {
    navigation<EditorGraph>(startDestination = Editor) {

        composable<Editor> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(EditorGraph)
            }
            val viewModel: EditorViewModel = viewModel(
                viewModelStoreOwner = parentEntry
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            EditorScreen(
                state = state,
                navController,
                onEvent = viewModel::onEvent,
            )
        }

        composable<EditMapScreen> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(EditorGraph)
            }
            val viewModel: EditorViewModel = viewModel(
                viewModelStoreOwner = parentEntry
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            val effect by viewModel.effect.collectAsState(initial = null)
            LaunchedEffect(effect) {
                when(effect) {
                    EditorEffect.NavigateToEditPoint -> navController.navigate(EditPointScreen)
                    null -> {}
                }
            }
            EditMapScreen(
                state = state,
                onEvent = viewModel::onEvent
            )
        }

        composable<EditPointScreen>{backStackEntry->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(EditorGraph)
            }
            val viewModel: EditorViewModel = viewModel(
                viewModelStoreOwner = parentEntry
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            EditPointScreen(state = state,navController,viewModel::onEvent)
        }
    }
}


fun NavGraphBuilder.mapGraph(navController: NavHostController) {
    navigation<MapGraph>(startDestination = Map) {

        composable<Map>(
            enterTransition = { slideInVertically() { it } },
            exitTransition = { slideOutVertically() { -it } },
            popEnterTransition = { slideInVertically() { -it } },
            popExitTransition = { slideOutVertically() { it } }
        ) { backStackEntry ->
            val viewModel: MapViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            MapScreen(
                state = state,
                onRouteClick = { routeId ->
                    navController.navigate(RouteInfo(routeId))
                }
            )
        }

        composable<RouteInfo>(
            enterTransition = { slideInVertically { it } },
            exitTransition = { slideOutVertically { it } }
        )
            { backStackEntry ->
            val routeId = backStackEntry.toRoute<RouteInfo>().routeId
            val viewModel: RouteInfoViewModel = koinViewModel(
                parameters = { parametersOf(routeId) }
            )
            val effect by viewModel.effect.collectAsState(initial = null)

            LaunchedEffect(effect) {
                if (effect == RouteInfoEffect.NavigateBack) {
                    navController.popBackStack()
                }
            }

            val state by viewModel.state.collectAsState()
            RouteInfoScreen(
                state = state,
                onEvent = viewModel::onEvent
            )
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
