package com.example.vk_android_vkat

import android.R.attr.padding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vk_android_vkat.features.explore.ExploreScreenTopBar
import com.example.vk_android_vkat.features.navigation.AuthGraph
import com.example.vk_android_vkat.features.navigation.BottomNavigationBar
import com.example.vk_android_vkat.features.navigation.Explore
import com.example.vk_android_vkat.features.navigation.RootNavGraph

@Composable
fun MyMapsApp() {
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    val currentRoute = entry?.destination?.route


    Scaffold(
        topBar = {
            when (currentRoute) {
                Explore::class.qualifiedName -> ExploreScreenTopBar()
                else -> null
            }
        },
        bottomBar = {
            if (entry?.destination?.hierarchy?.any{it.hasRoute(AuthGraph::class) } == false ){
                BottomNavigationBar(navController)
            }
        }
    ) { padding ->
        RootNavGraph(
            navController = navController,
            isUserLoggedIn = false,
            modifier = Modifier.padding(padding)
        )
    }
}