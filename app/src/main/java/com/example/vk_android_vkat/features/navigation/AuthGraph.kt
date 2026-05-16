package com.example.vk_android_vkat.features.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.vk_android_vkat.features.auth.login.LoginScreen
import com.example.vk_android_vkat.features.auth.login.LoginViewModel
import com.example.vk_android_vkat.features.auth.recovery.PasswordRecoveryScreen
import com.example.vk_android_vkat.features.auth.recovery.RecoveryViewModel
import com.example.vk_android_vkat.features.auth.registration.RegistrationScreen
import com.example.vk_android_vkat.features.auth.registration.RegistrationViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.authGraph(navController: NavController) {

    navigation<AuthGraph>(startDestination = Login) {

        // ------------------- LOGIN SCREEN -------------------
        composable<Login> { backStackEntry ->

            val viewModel: LoginViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()
            val effect by viewModel.effect.collectAsState(initial = null)

            // Экран только отображает состояние и кидает события
            LoginScreen(state = state, onEvent = viewModel::onEvent)

            // NavHost слушает эффекты и делает навигацию
            LaunchedEffect(effect) {
                when (effect) {
                    LoginViewModel.LoginEffect.LoginSuccess -> {
                        navController.navigate(MainGraph) {
                            popUpTo(AuthGraph) { inclusive = true }
                        }
                    }

                    LoginViewModel.LoginEffect.GoToRegistration -> navController.navigate(
                        Registration
                    )

                    LoginViewModel.LoginEffect.GoToPasswordRecovery -> navController.navigate(
                        PasswordRecovery
                    )

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
                state = state, onEvent = viewModel::onEvent
            )

            LaunchedEffect(effect) {
                when (effect) {
                    RegistrationViewModel.RegistrationEffect.RegistrationSuccess -> {
                        navController.navigate(MainGraph) {
                            popUpTo(AuthGraph) { inclusive = true }
                        }
                    }

                    RegistrationViewModel.RegistrationEffect.GoToLogin -> navController.navigate(
                        Login
                    )

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
                state = state, onEvent = viewModel::onEvent
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
}
