package com.example.vk_android_vkat.features.auth.recovery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.common.theme.BodyText
import com.example.vk_android_vkat.common.theme.EmailField
import com.example.vk_android_vkat.common.theme.HeadingText
import com.example.vk_android_vkat.common.theme.PrimaryButton
import com.example.vk_android_vkat.common.theme.TextButton

@Preview(showBackground = true)
@Composable
fun ForgotPasswordPreview(){
    PasswordRecoveryScreen(
        state = RecoveryState(),
        onEvent = {},
    )
}

@Composable
fun PasswordRecoveryScreen(
    state: RecoveryState,
    onEvent: (RecoveryEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadingText(
            text = stringResource(R.string.password_recovery),
        )

        Spacer(modifier = Modifier.height(16.dp))

        BodyText(
            text = stringResource(R.string.enter_the_email_for_password_recovery),
        )

        Spacer(modifier = Modifier.height(16.dp))

        EmailField(
            value = state.email,
            onValueChange = { onEvent(RecoveryEvent.EmailChanged(it)) },
            errorMessage = state.emailError?.let { stringResource(it.resId) },
            imeAction = ImeAction.Next,
            onImeAction = {}
        )
        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = stringResource(R.string.send_link),
            onClick = { onEvent(RecoveryEvent.SendRecoveryClicked) },
            enabled = state.email.isNotBlank()
        )

        TextButton(
            text = stringResource(R.string.return_to_sign_in),
            onClick = { onEvent(RecoveryEvent.LoginClicked) },
        )
    }
}