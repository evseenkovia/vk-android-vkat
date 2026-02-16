package com.example.vk_android_vkat.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.vk_android_vkat.ui.auth.login.LoginScreen
import com.example.vk_android_vkat.ui.auth.login.LoginViewModel
import com.example.vk_android_vkat.ui.auth.recovery.PasswordRecoveryScreen
import com.example.vk_android_vkat.ui.auth.recovery.RecoveryViewModel
import com.example.vk_android_vkat.ui.auth.registration.RegistrationScreen
import com.example.vk_android_vkat.ui.auth.registration.RegistrationViewModel

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

        navigation<AuthGraph>(startDestination = Login) {

            // ------------------- LOGIN SCREEN -------------------
            composable<Login> { backStackEntry ->
                val viewModel: LoginViewModel = viewModel()
                val state by viewModel.state.collectAsState()
                val effect by viewModel.effect.collectAsState(initial = null)

                // Экран только отображает состояние и кидает события
                LoginScreen(
                    state = state,
                    onEvent = viewModel::onEvent
                )

                // NavHost слушает эффекты и делает навигацию
                LaunchedEffect(effect) {
                    when (effect) {
                        LoginViewModel.LoginEffect.LoginSuccess -> {
                            navController.navigate(MainGraph) {
                                popUpTo(AuthGraph) { inclusive = true }
                            }
                        }
                        LoginViewModel.LoginEffect.GoToRegistration -> navController.navigate(Registration)
                        LoginViewModel.LoginEffect.GoToPasswordRecovery -> navController.navigate(PasswordRecovery)
                        null -> {}
                    }
                }
            }

            // ------------------- REGISTRATION SCREEN -------------------
            composable<Registration> { backStackEntry ->
                val viewModel: RegistrationViewModel = viewModel()
                val state by viewModel.state.collectAsState()
                val effect by viewModel.effect.collectAsState(initial = null)

                RegistrationScreen(
                    state = state,
                    onEvent = viewModel::onEvent
                )

                LaunchedEffect(effect) {
                    when (effect) {
                        RegistrationViewModel.RegistrationEffect.RegistrationSuccess -> {
                            navController.navigate(MainGraph) {
                                popUpTo(AuthGraph) { inclusive = true }
                            }
                        }
                        RegistrationViewModel.RegistrationEffect.GoToLogin -> navController.navigate(Login)
                        null -> {}
                    }
                }
            }

            // ------------------- PASSWORD RECOVERY SCREEN -------------------
            composable<PasswordRecovery> { backStackEntry ->
                val viewModel: RecoveryViewModel = viewModel()
                val state by viewModel.state.collectAsState()
                val effect by viewModel.effect.collectAsState(initial = null)

                PasswordRecoveryScreen(
                    state = state,
                    onEvent = viewModel::onEvent
                )

                LaunchedEffect(effect) {
                    when (effect) {
                        null -> {}
                        RecoveryViewModel.RecoveryEffect.GoToLogin -> navController.navigate(Login)
                        RecoveryViewModel.RecoveryEffect.EmailSent -> navController.navigate(Login)
                    }
                }
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
