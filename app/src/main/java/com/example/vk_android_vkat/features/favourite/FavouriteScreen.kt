package com.example.vk_android_vkat.ui.editor

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import android.net.Uri
import coil.compose.rememberAsyncImagePainter

@Composable
fun EditorScreen() {
    var routeName by rememberSaveable { mutableStateOf("") }
    var routeDescription by rememberSaveable { mutableStateOf("") }
    val scrollState = rememberScrollState()

    // Сохраняем URI как строку, чтобы переживать повороты экрана
    var selectedImageUriString by rememberSaveable { mutableStateOf<String?>(null) }
    val selectedImageUri = selectedImageUriString?.let { Uri.parse(it) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUriString = uri?.toString()
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Создание маршрута",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clickable {
                        galleryLauncher.launch("image/*")
                    },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (selectedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
                            contentDescription = "Выбранное фото маршрута",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "Прикрепить фото",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Прикрепить фото",
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }
            }

            OutlinedTextField(
                value = routeName,
                onValueChange = { routeName = it },
                label = { Text("Название маршрута") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = routeDescription,
                onValueChange = { routeDescription = it },
                label = { Text("Описание маршрута") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Button(
                onClick = { /* добавление точки */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Добавить точку")
            }
        }
    }
}