package com.example.vk_android_vkat.ui.auth

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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vk_android_vkat.R

@Preview(showBackground = true)
@Composable
fun ForgotPasswordPreview(){
    PasswordRecoveryScreen(
        state = AuthState(mode = AuthMode.PasswordRecovery),
        onEvent = {},
        onNavigate = {}
    )
}

@Composable
fun PasswordRecoveryScreen(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit,
    onNavigate: (AuthMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок
        Text(
            text = stringResource(R.string.password_recovery),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Описание
        Text(
            text = stringResource(R.string.enter_the_email_for_password_recovery),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Поле email
        OutlinedTextField(
            value = state.email,
            onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            isError = state.emailError != null,
            trailingIcon = {
                Icon(imageVector = Icons.Filled.Email,
                    contentDescription = stringResource(R.string.email))
            }
        )
        state.emailError?.let {
            Text(
                text = stringResource(it),
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка отправки
        Button(
            onClick = { /* TODO(Реализовать отправку ссылки на почту) */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = state.email.isNotBlank()
        ) {
            Text(stringResource(R.string.send_link))
        }

        // Кнопка возврата
        TextButton(
            onClick = { onNavigate(AuthMode.Login) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.return_to_sign_in))
        }
    }
}