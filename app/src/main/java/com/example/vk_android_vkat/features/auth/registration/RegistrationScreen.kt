package com.example.vk_android_vkat.features.auth.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.common.theme.EmailField
import com.example.vk_android_vkat.common.theme.LabelText
import com.example.vk_android_vkat.common.theme.NameField
import com.example.vk_android_vkat.common.theme.PasswordField
import com.example.vk_android_vkat.common.theme.PrimaryButton
import com.example.vk_android_vkat.common.theme.SecondaryButton

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview(){
    RegistrationScreen(
        state = RegistrationState(),
        onEvent = {},
    )
}

@Composable
fun RegistrationScreen(
    state: RegistrationState,
    onEvent: (RegistrationEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.registration),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            NameField(
                value = state.name,
                onValueChange = { onEvent(RegistrationEvent.NameChanged(it)) },
//                errorMessage = state.emailError?.let { stringResource(it.resId) },
                imeAction = ImeAction.Next
            )
            Spacer(modifier = Modifier.height(16.dp))

            EmailField(
                value = state.email,
                onValueChange = { onEvent(RegistrationEvent.EmailChanged(it)) },
                errorMessage = state.emailError?.let { stringResource(it.resId) },
                imeAction = ImeAction.Next
            )
            Spacer(modifier = Modifier.height(16.dp))

            PasswordField(
                value = state.password,
                onValueChange = { onEvent(RegistrationEvent.PasswordChanged(it)) },
                errorMessage = state.passwordError?.let { stringResource(it.resId) },
                imeAction = ImeAction.Next
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordField(
                label = stringResource(R.string.repeat_password),
                value = state.confirmPassword,
                onValueChange = { onEvent(RegistrationEvent.ConfirmPasswordChanged(it)) },
                errorMessage = state.confirmPasswordError?.let { stringResource(it.resId) },
                imeAction = ImeAction.Next
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = stringResource(R.string.sign_up),
                onClick = { onEvent(RegistrationEvent.RegisterClicked) },
                enabled = state.email.isNotBlank() &&
                        state.password.isNotBlank() &&
                        state.confirmPassword.isNotBlank() &&
                        state.name.isNotBlank()
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LabelText(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(R.string.already_have_an_account),
            )

            SecondaryButton(
                text = stringResource(R.string.sign_in_do),
                onClick = { onEvent(RegistrationEvent.LoginClicked) }
            )
        }
    }
}