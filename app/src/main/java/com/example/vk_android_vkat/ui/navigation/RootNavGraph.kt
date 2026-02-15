package com.example.vk_android_vkat.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.vk_android_vkat.ui.add.EditorScreen
import com.example.vk_android_vkat.ui.auth.AuthMode
import com.example.vk_android_vkat.ui.auth.AuthViewModel
import com.example.vk_android_vkat.ui.auth.LoginScreen
import com.example.vk_android_vkat.ui.auth.PasswordRecoveryScreen
import com.example.vk_android_vkat.ui.auth.RegistrationScreen
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

@Composable
fun RootNavGraph(
    navController: NavHostController,
    modifier : Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isUserLoggedIn: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (!isUserLoggedIn) AuthGraph else MainGraph,
        modifier = modifier.fillMaxSize(),
        ) {

        navigation<AuthGraph>(startDestination = Login){

            composable<Login> { backStackEntry ->
                val viewModel: AuthViewModel = viewModel()
                val state by viewModel.state.collectAsState()
                LoginScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigate = { target ->
                        when (target) {
                            AuthMode.Registration -> navController.navigate(Registration)
                            AuthMode.PasswordRecovery -> navController.navigate(PasswordRecovery)
                            else -> {}
                        }
                    },
                    onLoginSuccess = {
                        navController.navigate(MainGraph) {
                            popUpTo(AuthGraph) { inclusive = true } // очищаем стек auth
                        }
                    }
                )
            }

            composable<Registration> {
                val viewModel: AuthViewModel = viewModel()
                val state by viewModel.state.collectAsState()
                RegistrationScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigate = { target ->
                        if (target == AuthMode.Login) navController.navigate(Login) {
                            popUpTo(Registration) { inclusive = true }
                        }
                    },
                    onRegisterSuccess = {
                        navController.navigate("main_graph") {
                            popUpTo(AuthGraph) { inclusive = true }
                        }
                    }
                )
            }

            composable<PasswordRecovery> {
                val viewModel: AuthViewModel = viewModel()
                val state by viewModel.state.collectAsState()
                PasswordRecoveryScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigate = { target ->
                        if (target == AuthMode.Login) navController.navigate(Login) {
                            popUpTo(PasswordRecovery) { inclusive = true }
                        }
                    }
                )
            }
        }

        navigation<MainGraph>(startDestination = SearchGraph){
            searchGraph()
            favouriteGraph()
            editorGraph()
            mapGraph()
            profileGraph()
        }
    }
}
