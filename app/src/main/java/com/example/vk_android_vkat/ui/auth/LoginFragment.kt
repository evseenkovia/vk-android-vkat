package com.example.vk_android_vkat.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vk_android_vkat.R

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
            val state by viewModel.state.collectAsState()

                // Условный рендеринг
                when {
                    // Показываем сплеш пока проверяем авторизацию
                    state.isLoading || !state.isAuthChecked -> {
                        SplashScreen()
                    }

                    // Если пользователь уже залогинен - переходим на главный
                    state.isUserLoggedIn -> {
                        LaunchedEffect(Unit) {
                            navigateToMain()
                        }
                        SplashScreen() // Показываем сплеш во время навигации
                    }

                    // Иначе показываем экран авторизации
                    else -> {
                        AuthScreen(
                            state = state,
                            viewModel = viewModel,
                            onLoginSuccess = { navigateToMain() }
                        )
                    }
                }
            }
        }
    }

    private fun navigateToMain(){
        findNavController().navigate(R.id.navigation_explore)
    }
}