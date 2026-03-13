package com.example.vk_android_vkat.features.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.example.vk_android_vkat.features.explore.ExploreViewModel


@Composable
fun RootNavGraph(
    navController: NavHostController,
    isUserLoggedIn: Boolean,
) {

    val sharedViewModel: ExploreViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = if (!isUserLoggedIn) AuthGraph else MainGraph,
    ) {

        // авторизация
        authGraph(navController)

        // основной граф
        navigation<MainGraph>(startDestination = ExploreGraph){

            exploreGraph(navController,sharedViewModel)
            favouriteGraph(navController,sharedViewModel)
            editorGraph(navController)
            mapGraph(navController)
            profileGraph(navController)
        }
    }
}
