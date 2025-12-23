package com.example.vk_android_vkat.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Твой логотип
//            Icon(
//                imageVector = Icons.Default.AccountCircle,
//                contentDescription = "Logo",
//                tint = Color.White,
//                modifier = Modifier.size(64.dp)
//            )

//            Spacer(modifier = Modifier.height(16.dp))

            // Индикатор загрузки
            CircularProgressIndicator(color = Color.White)
        }
    }
}