package com.example.vk_android_vkat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vk_android_vkat.ui.navigation.AuthGraph
import com.example.vk_android_vkat.ui.navigation.RootNavGraph
import com.example.vk_android_vkat.ui.navigation.bottomNavDestinations

@Composable
fun MyMapsApp() {
    val navController = rememberNavController()
    // Следим за бэкстеком
    val entry by navController.currentBackStackEntryAsState()
    val currentDestination = entry?.destination
    // Отображаем панель навигации только на главном экране
    val isAuthScreen = currentDestination?.hierarchy?.any { it.hasRoute(AuthGraph::class) } == true

    Scaffold(
        bottomBar = {
            if (!isAuthScreen){
                NavigationBar(
                    tonalElevation = 4.dp
                ) {

                    //Через список отображаем все табы
                    bottomNavDestinations.forEach { tab ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any {it.hasRoute(tab.graphRoute::class)} == true,
                            onClick = {
                                navController.navigate(tab.graphRoute) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                }
                            },
                            icon = { tab.IconComposable() },
                            label = { Text(stringResource(tab.labelRes)) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        RootNavGraph(
            navController =  navController,
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding,
            isUserLoggedIn = false
        )
    }
}