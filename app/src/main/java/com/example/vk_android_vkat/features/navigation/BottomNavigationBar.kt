package com.example.vk_android_vkat.features.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vk_android_vkat.common.theme.navigationBarItemColors
import com.google.android.material.color.MaterialColors

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        tonalElevation = 4.dp,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary,



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
                icon = {
                    if (tab.graphRoute == EditorGraph) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            tab.IconComposable(
                                isSelected,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    } else {
                        tab.IconComposable(
                            isSelected,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },

                colors = if (tab.graphRoute == EditorGraph) {
                    navigationBarItemColors.copy(
                        selectedIconColor = MaterialTheme.colorScheme.background,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary,

                    )
                } else {
                    navigationBarItemColors
                }
            )
        }
    }
}