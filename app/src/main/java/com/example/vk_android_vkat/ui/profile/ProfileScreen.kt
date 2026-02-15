package com.example.vk_android_vkat.ui.profile

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.vk_android_vkat.R

//@Preview(showBackground = true)
//@Composable
//fun PreviewMainScreen(){
//    ProfileScreen(ProfileViewModel())
//}

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit
) {
    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (uiState) {
                is ProfileUiState.Loading -> LoadingState()
                is ProfileUiState.Error -> ErrorState(
                    errorMessage = uiState.message,
                    onRetry = { onEvent(ProfileUiEvent.Retry) }
                )
                is ProfileUiState.Content -> ContentState(
                    content = uiState.data,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
fun LoadingState() = Box(
    Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) { CircularProgressIndicator() }

@Composable
fun ErrorState(errorMessage: String, onRetry: () -> Unit) = Box(
    Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(errorMessage, color = Color.Red)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) { Text("Try again") }
    }
}

@Composable
fun ContentState(
    content: ProfileContentUi,
    onEvent: (ProfileUiEvent) -> Unit
){
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Заголовок профиля
        item {
            ProfileHeader(
                name = content.header.userName,
                email = content.header.email,
                avatarUrl = content.header.avatarUrl
            )
        }

        // Разделы настроек
        content.sections.forEach { item ->
            item {
                when (item) {
                    is ProfileItemUi.Info -> InfoItem(item)
                    is ProfileItemUi.Switch -> SwitchItem(item, onEvent)
                    is ProfileItemUi.Navigation -> NavigationItem(item, onEvent)
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(
    name: String,
    email: String,
    avatarUrl: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = email,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun InfoItem(item: ProfileItemUi.Info) {
    ListItem(
        headlineContent = { Text(item.title) },
        supportingContent = { item.subtitle?.let { Text(it) } }
    )
}

@Composable
fun SwitchItem(item: ProfileItemUi.Switch, onEvent: (ProfileUiEvent) -> Unit) {
    ListItem(
        headlineContent = { Text(item.title) },
        trailingContent = {
            Switch(
                checked = item.checked,
                onCheckedChange = { onEvent(ProfileUiEvent.SwitchChanged(item, it)) }
            )
        }
    )
}

@Composable
fun NavigationItem(item: ProfileItemUi.Navigation, onEvent: (ProfileUiEvent) -> Unit) {
    ListItem(
        headlineContent = { Text(item.title) },
        modifier = Modifier.clickable { onEvent(ProfileUiEvent.ItemClicked(item)) }
    )
}