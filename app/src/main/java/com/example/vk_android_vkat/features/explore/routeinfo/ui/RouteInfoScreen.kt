package com.example.vk_android_vkat.features.explore.routeinfo.ui

import androidx.compose.foundation.BorderStroke
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.features.editor.domain.RoutePointModel
import com.example.vk_android_vkat.features.explore.domain.RouteModel

@Composable
fun RouteInfoScreen(
    state: RouteInfoState,
    onEvent: (RouteInfoEvent) -> Unit
) {
    Scaffold(contentWindowInsets = WindowInsets(0, 0, 0, 0)) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) {
            when {
                state.isLoading -> LoadingScreenState()
                !state.error.isNullOrEmpty() -> ErrorScreenState(state)
                state.routeData != null -> RouteInfoLoadedScreenState(state, onEvent)
            }
        }
    }
}

@Composable
fun LoadingScreenState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreenState(state: RouteInfoState) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Что-то пошло не так(((")
        ElevatedButton(
            onClick = {},
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text("На главный экран")
        }
    }
}

@Composable
fun RouteInfoLoadedScreenState(
    state: RouteInfoState,
    onEvent: (RouteInfoEvent) -> Unit
) {
    val route = state.routeData ?: return
    var selectedPoint by remember { mutableStateOf<RoutePointModel?>(null) }
    val localContext = LocalContext.current

    val titleTextSize = MaterialTheme.typography.titleLarge
    val bodyTextSize = MaterialTheme.typography.bodyLarge
    val iconSize = 24.dp
    val interfaceColor = Color.Black

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    model = route.imageUrl,
                    contentDescription = "Image for route with id = ${route.id}",
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                )
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .align(Alignment.TopEnd)
                        .padding(top = 12.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_comment),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { onEvent(RouteInfoEvent.ToggleFavourite) }) {
                        Icon(
                            painter = if (route.isFavourite)
                                painterResource(R.drawable.ic_bookmark_filled)
                            else
                                painterResource(R.drawable.ic_bookmark_32dp),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(text = route.title, style = titleTextSize, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${route.distanceKm} км | ${route.durationHours} ч",
                        style = bodyTextSize,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.StarBorder,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(iconSize)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = route.rating.toString(), color = Color.White, style = bodyTextSize)
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(iconSize)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${route.pointsCount} точек", color = Color.White, style = bodyTextSize)
                    }
                }
            }
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = route.description,
                    style = bodyTextSize,
                    color = interfaceColor,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (route.points.isNotEmpty()) {
                    Text(
                        text = "Точки маршрута (${route.points.size})",
                        style = MaterialTheme.typography.titleMedium,
                        color = interfaceColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    route.points.forEach { point ->
                        PointItem(
                            point = point,
                            onClick = { selectedPoint = point }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
        IconButton(
            onClick = { onEvent(RouteInfoEvent.BackClicked) },
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 12.dp, start = 16.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.background
            )
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(0.9f),
            onClick = {openRouteInYandexMaps(localContext,state.routeData)},
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Text(
                text = "Открыть в картах",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        if (selectedPoint != null) {
            AlertDialog(
                onDismissRequest = { selectedPoint = null },
                title = { Text(selectedPoint!!.pointName.ifBlank { "Точка маршрута" }) },
                text = { Text(selectedPoint!!.pointDescription.ifBlank { "Описание отсутствует" }) },
                confirmButton = {
                    TextButton(onClick = { selectedPoint = null }) {
                        Text("Закрыть")
                    }
                }
            )
        }
    }
}

fun openRouteInYandexMaps(
    context: Context,
    route: RouteModel
) {
    val points = route.points
    if (points.isEmpty()) return

    val rtext = points.joinToString("~") { point ->
        "${point.latitude},${point.longitude}"
    }

    val uri = "https://yandex.ru/maps/?rtext=$rtext&rtt=pd".toUri()

    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("ru.yandex.yandexmaps")
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}
@Composable
fun PointItem(point: RoutePointModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.size(72.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (point.photoUri != null) {
                    AsyncImage(
                        model = point.photoUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = point.pointName.ifBlank { "Точка маршрута" },
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = point.address.ifBlank { "Адрес не указан" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}