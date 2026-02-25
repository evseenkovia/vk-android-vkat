package com.example.vk_android_vkat.features.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.vk_android_vkat.features.auth.login.LoginScreen
import com.example.vk_android_vkat.features.auth.login.LoginViewModel
import com.example.vk_android_vkat.features.auth.recovery.PasswordRecoveryScreen
import com.example.vk_android_vkat.features.auth.recovery.RecoveryViewModel
import com.example.vk_android_vkat.features.auth.registration.RegistrationScreen
import com.example.vk_android_vkat.features.auth.registration.RegistrationViewModel
import com.example.vk_android_vkat.features.explore.ExploreScreen
import com.example.vk_android_vkat.features.explore.ExploreViewModel
import com.example.vk_android_vkat.features.explore.data.RouteRepositoryMock
import com.example.vk_android_vkat.features.explore.routeinfo.ui.RouteInfoScreen
import com.example.vk_android_vkat.features.explore.routeinfo.ui.RouteInfoViewModel
import com.example.vk_android_vkat.features.navigation.editorGraph
import com.example.vk_android_vkat.features.navigation.favouriteGraph
import com.example.vk_android_vkat.features.navigation.mapGraph
import com.example.vk_android_vkat.features.navigation.profileGraph


@Composable
fun RootNavGraph(
    navController: NavHostController,
    isUserLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = if (!isUserLoggedIn) AuthGraph else MainGraph,
        modifier = modifier
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
