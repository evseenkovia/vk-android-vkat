package com.example.vk_android_vkat.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
import kotlinx.serialization.Serializable

//------ Графы навигации для табов ------
@Serializable
object SearchGraph
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

//------ Расширения для графов ------
fun NavGraphBuilder.searchGraph(){
    navigation<SearchGraph>(startDestination = Search){

        composable<Search> {
            val viewModel: ExploreViewModel = viewModel()
            val uiState by viewModel.state.collectAsState()

            SearchScreen(
                uiState = uiState,
                onEvent = { event ->
                    viewModel.onEvent(event)
                    when (event) {
                        is SearchUiEvent.RouteClicked -> {}//navController.navigate(RouteDetails(event.routeId))
                        is SearchUiEvent.FilterClicked -> TODO("Логика фильтров")
                        SearchUiEvent.Retry -> TODO()
                    }
                }
            )
        }
    }
}

fun NavGraphBuilder.favouriteGraph(){
    navigation<FavouriteGraph>(startDestination = Favourite){

        composable<Favourite> { backStackEntry ->
            val details = backStackEntry.toRoute<Favourite>()
            FavouriteScreen()
        }
    }
}

fun NavGraphBuilder.editorGraph(){
    navigation<EditorGraph>(startDestination = Editor){

        composable<Editor> { backStackEntry ->
            val details = backStackEntry.toRoute<Editor>()
            EditorScreen()
        }
    }
}

fun NavGraphBuilder.mapGraph(){
    navigation<MapGraph>(startDestination = Map){

        composable<Map> { backStackEntry ->
            val details = backStackEntry.toRoute<Map>()
            MapScreen()
        }
    }
}

fun NavGraphBuilder.profileGraph(){
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

//------ Основные экраны ------
@Serializable
object Search
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
data class RouteDetails(val routeId: Long)
