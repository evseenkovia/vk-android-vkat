package com.example.vk_android_vkat.features.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.features.editor.domain.RoutePointModel
import com.example.vk_android_vkat.features.navigation.EditMapScreen
import com.example.vk_android_vkat.features.navigation.ScreenTeg

@Composable
fun EditorScreen(
    state: EditorState,
    navController: NavHostController,
    onEvent: (EditorEvent) -> Unit
) {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onEvent(EditorEvent.ImageSelected(uri))
    }

    // Проверка, что все обязательные поля маршрута заполнены
    val isRouteFormValid = state.routeName.trim().isNotEmpty() &&
            state.routeDescription.trim().isNotEmpty() &&
            state.selectedImageUri != null

    // Проверка, что добавлена хотя бы одна точка
    val hasPoints = state.points.isNotEmpty()

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {

            item {
                Text(
                    text = stringResource(R.string.route_creation),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                        .clickable { galleryLauncher.launch("image/*") },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (state.selectedImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(state.selectedImageUri),
                                contentDescription = stringResource(R.string.selected_route_photo),
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = stringResource(R.string.attach_photo),
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = stringResource(R.string.attach_photo),
                                modifier = Modifier.align(Alignment.BottomCenter)
                            )
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = state.routeName,
                    onValueChange = { onEvent(EditorEvent.RouteNameChanged(it)) },
                    label = { Text(stringResource(R.string.route_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = state.routeDescription,
                    onValueChange = { onEvent(EditorEvent.RouteDescriptionChanged(it)) },
                    label = { Text(stringResource(R.string.route_description)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }

            // список точек
            itemsIndexed(state.points) { index, point ->
                RoutePointItem(
                    index = index,
                    point = point,
                    onDelete = { onEvent(EditorEvent.RemovePoint(index))}
                )
            }

            item {
                Button(
                    onClick = { navController.navigate(EditMapScreen) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isRouteFormValid
                ) {
                    Text(stringResource(R.string.add_point))
                }
            }

            // Кнопка "Завершить" появляется только если есть хотя бы одна точка
            // ... внутри LazyColumn

            if (hasPoints) {
                item {
                    Button(
                        onClick = {
                            navController.navigate(ScreenTeg)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.finish_route))
                    }
                }
            }
        }
    }
}

@Composable
fun RoutePointItem(
    index: Int,
    point: RoutePointModel,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Левая часть — картинка (маленькая)
            Card(
                modifier = Modifier.size(72.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (point.photoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(point.photoUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }

            // Правая часть — текст
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = point.pointName.ifBlank { stringResource(R.string.point_is, index + 1) },
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = point.address.ifBlank { stringResource(R.string.address_is_not_stated) },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Кнопка удаления
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить"
                )
            }
        }
    }
}