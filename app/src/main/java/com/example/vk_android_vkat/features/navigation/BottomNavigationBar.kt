package com.example.vk_android_vkat.features.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        tonalElevation = 4.dp
    ) {
        val entry by navController.currentBackStackEntryAsState()
        val currentDestination = entry?.destination

        //Через список отображаем все табы
        bottomNavDestinations.forEach { tab ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(tab.graphRoute::class)
            } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(tab.graphRoute) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    }
                },
                icon = { tab.IconComposable(isSelected) },
                label = { Text(text = stringResource(tab.labelRes), maxLines = 1 ) }
            )
        }
    }
}