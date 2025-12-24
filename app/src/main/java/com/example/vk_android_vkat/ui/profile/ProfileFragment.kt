package com.example.vk_android_vkat.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.vk_android_vkat.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private val binding get() = _binding!!

    private var _binding: FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

//        _binding = FragmentProfileBinding.inflate(inflater, container, false)
//        val root: View = binding.root


        return ComposeView(requireContext()).apply {
            setContent {
                ProfileScreen(profileViewModel)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewMainScreen(){
        ProfileScreen(ProfileViewModel())
    }

    @Composable
    fun ProfileScreen(
        viewModel: ProfileViewModel
    ) {
        val state by viewModel.state.collectAsState()
        LazyColumn {
            item {
                ProfileHeader(
                    name = state.name,
                    email = state.email,
                    avatarUrl = state.avatarUrl
                )
            }

            item { ProfileSection("Аккаунт") }

            item {
                ProfileItem(
                    title = "Email",
                    subtitle = state.email,
                    icon = Icons.Filled.Email
                )
            }

            item {
                ProfileItem(
                    title = "Изменить пароль",
                    icon = Icons.Filled.Lock
                )
            }

            item { ProfileSection("Настройки") }

            item {
                ProfileItem(
                    title = "Уведомления",
                    icon = Icons.Filled.Notifications,
                    trailing = {
                        Switch(checked = state.notificationsEnabled, onCheckedChange = {})
                    }
                )
            }

            item {
                ProfileItem(
                    title = "Тёмная тема",
                    icon = Icons.Filled.DarkMode,
                    trailing = {
                        Switch(checked = state.darkThemeEnabled, onCheckedChange = {})
                    }
                )
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
    fun ProfileSection(title: String) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }

    @Composable
    fun ProfileItem(
        title: String,
        subtitle: String? = null,
        icon: ImageVector,
        trailing: @Composable (() -> Unit)? = null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = false) {} // задел на будущее
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge)
                subtitle?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            trailing?.invoke()
        }
    }
}