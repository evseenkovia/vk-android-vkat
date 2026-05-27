package com.example.vk_android_vkat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vk_android_vkat.features.navigation.AuthGraph
import com.example.vk_android_vkat.features.navigation.BottomNavigationBar
import com.example.vk_android_vkat.features.navigation.RootNavGraph

@Composable
fun MyMapsApp() {
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            if (entry?.destination?.hierarchy?.any{it.hasRoute(AuthGraph::class) } == false ){
                BottomNavigationBar(navController)
            }
        }
    ) { padding ->
        val bottomInset = padding.calculateBottomPadding()
        Box(
            modifier = Modifier.fillMaxSize().padding(bottom = bottomInset)
        ){
            RootNavGraph(
                navController = navController,
                isUserLoggedIn = false,
            )
        }
    }
}