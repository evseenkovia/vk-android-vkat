package com.example.vk_android_vkat.features.auth.login

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.common.theme.EmailField
import com.example.vk_android_vkat.common.theme.LabelText
import com.example.vk_android_vkat.common.theme.PasswordField
import com.example.vk_android_vkat.common.theme.PrimaryButton
import com.example.vk_android_vkat.common.theme.SecondaryButton
import com.example.vk_android_vkat.common.theme.TextButton
import com.example.vk_android_vkat.config.UseToken
import com.example.vk_android_vkat.config.getOneTapFailCallback
import com.example.vk_android_vkat.config.getOneTapSuccessCallback
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.compose.onetap.OneTap

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        state = LoginState(),
        onEvent = {}
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.sign_in),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            EmailField(
                value = state.email,
                onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
                errorMessage = state.emailError?.let { stringResource(it.resId) },
                imeAction = ImeAction.Next,
                onImeAction = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordField(
                value = state.password,
                onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                errorMessage = state.passwordError?.let { stringResource(it.resId) },
                imeAction = ImeAction.Done,
                onImeAction = { onEvent(LoginEvent.LoginClicked) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // кнопка VK ID
            val context = LocalContext.current
            var token: AccessToken? by remember { mutableStateOf(null) }

            OneTap(
                modifier = Modifier.fillMaxWidth(),
                style = if (isSystemInDarkTheme()) {
                    OneTapStyle.Dark(cornersStyle = OneTapButtonCornersStyle.Rounded)
                } else {
                    OneTapStyle.Light(cornersStyle = OneTapButtonCornersStyle.Rounded)
                },
                onAuth = { oAuth, accessToken ->
                    onEvent(LoginEvent.VKAuthSuccess(accessToken))
                },
                onFail = { oAuth, fail ->
                    val errorMessage = when (fail) {
                        is VKIDAuthFail.Canceled -> "Авторизация отменена"
                        is VKIDAuthFail.FailedOAuth -> "Ошибка OAuth"
                        is VKIDAuthFail.FailedApiCall -> "Ошибка сети или API"
                        else -> "Неизвестная ошибка"
                    }
                    onEvent(LoginEvent.VKAuthError(errorMessage))
                },
                signInAnotherAccountButtonEnabled = true,
                oAuths = setOf(OneTapOAuth.MAIL, OneTapOAuth.OK),
            )
            token?.let {
                Spacer(modifier = Modifier.height(32.dp))
                UseToken(it)
            }

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = stringResource(R.string.sign_in_do),
                onClick = { onEvent(LoginEvent.LoginClicked) },
                enabled = state.email.isNotBlank() && state.password.isNotBlank()
            )

            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(R.string.forgot_password),
                onClick = { onEvent(LoginEvent.ForgotPasswordClicked) }
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LabelText(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(R.string.no_account),
            )

            SecondaryButton(
                text = stringResource(R.string.sign_up),
                onClick = { onEvent(LoginEvent.RegisterClicked) }
            )
        }
    }
}
