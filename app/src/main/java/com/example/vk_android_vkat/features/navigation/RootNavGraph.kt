package com.example.vk_android_vkat.features.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation


@Composable
fun RootNavGraph(
    navController: NavHostController,
    isUserLoggedIn: Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = if (!isUserLoggedIn) AuthGraph else MainGraph,
    ) {

        // авторизация
        authGraph(navController)

        // основной граф
        navigation<MainGraph>(startDestination = ExploreGraph){
            exploreGraph(navController)
            favouriteGraph(navController)
            editorGraph(navController)
            mapGraph(navController)
            profileGraph(navController)
        }
    }
}
