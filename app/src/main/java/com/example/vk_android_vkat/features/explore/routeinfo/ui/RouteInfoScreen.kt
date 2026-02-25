package com.example.vk_android_vkat.features.explore.routeinfo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.data.mockRoutes

@Preview(showBackground = true)
@Composable
fun RouteInfoScreenPreview(){
    RouteInfoScreen(
//        state = RouteInfoState.Loading,
//        state = RouteInfoState.Error("Что-то пошло не так((("),
        state = RouteInfoState.RouteInfoLoaded(mockRoutes[1]),
        onEvent = {},
        onBack = {}
    )
}

@Composable
fun RouteInfoScreen( //TODO(Реализовать экран для маршрута)
    state: RouteInfoState,
    onEvent: (RouteInfoEvent) -> Unit,
    onBack: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        when(state) {
            RouteInfoState.Loading -> LoadingScreenState()
            is RouteInfoState.Error -> ErrorScreenState(state)
            is RouteInfoState.RouteInfoLoaded -> RouteInfoLoadedScreenState(state, onEvent, onBack)
        }
    }
}

@Composable
fun LoadingScreenState(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreenState(
    state: RouteInfoState,
    onEvent: (RouteInfoEvent) -> Unit = {}
){

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Что-то пошло не так((("
        )
        ElevatedButton(
            onClick = {},
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "На главный экран"
            )
        }
    }
}

@Composable
fun RouteInfoLoadedScreenState(
    state: RouteInfoState,
    onEvent: (RouteInfoEvent) -> Unit,
    onBack: () -> Unit
){
    val currentState = state as RouteInfoState.RouteInfoLoaded
    val route = currentState.route

    val titleTextSize = MaterialTheme.typography.titleLarge
    val bodyTextSize = MaterialTheme.typography.bodyLarge
    val iconSize = 24.dp
    val interfaceColor = Color.Black

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Блок с изображением и кнопкой "назад" поверх него
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Изображение (позже заменится на карусель)
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f),
                    model = currentState.route.imageUrl,
                    contentDescription = "Image for route with id = ${state.route.id}",
                    contentScale = ContentScale.Crop
                )

                // Кнопка "назад" поверх изображения
                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.8f),
                            shape = CircleShape
                        ),
                    onClick = onBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = interfaceColor
                    )
                }
            }

            // Контент под изображением
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Название маршрута
                Text(
                    text = route.title,
                    style = titleTextSize,
                    color = interfaceColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Дистанция и время
                Text(
                    text = "${route.distanceKm} км | ${route.durationHours} ч",
                    style = bodyTextSize,
                    color = interfaceColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Рейтинг и количество точек в одной строке
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Рейтинг
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(iconSize),
                            imageVector = Icons.Outlined.StarBorder,
                            contentDescription = stringResource(R.string.rating),
                            tint = interfaceColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = route.rating.toString(),
                            style = bodyTextSize,
                            color = interfaceColor
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Количество точек
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(iconSize),
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Number of places",
                            tint = interfaceColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${route.pointsCount} точек",
                            style = bodyTextSize,
                            color = interfaceColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Описание
                Text(
                    text = route.description,
                    style = bodyTextSize,
                    color = interfaceColor
                )

                // Добавляем отступ внизу, чтобы контент не скрывался за FAB
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Кнопка "Открыть в картах" всегда внизу
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(0.9f),
            onClick = {},
        ) {
            Text(
                text = "Открыть в картах",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}