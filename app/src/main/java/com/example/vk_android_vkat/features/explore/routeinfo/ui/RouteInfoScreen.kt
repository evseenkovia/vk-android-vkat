package com.example.vk_android_vkat.features.explore.routeinfo.ui

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.res.painterResource
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
        onEvent = {}
    )
}

@Composable
fun RouteInfoScreen( //TODO(Реализовать экран для маршрута)
    state: RouteInfoState,
    onEvent: (RouteInfoEvent) -> Unit
    ){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        when(state) {
            RouteInfoState.Loading -> LoadingScreenState()
            is RouteInfoState.Error -> ErrorScreenState(state)
            is RouteInfoState.RouteInfoLoaded -> RouteInfoLoadedScreenState(state, onEvent)
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
    onEvent: (RouteInfoEvent) -> Unit
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
            IconButton(
                modifier = Modifier.align(Alignment.Start),
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f),
                model = currentState.route.imageUrl,
                contentDescription = "Image for route with id = ${state.route.id}",
                contentScale = ContentScale.Crop
            )

            //Название маршрута
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = route.title,
                style = titleTextSize,
                color = interfaceColor
            )
            // Дистанция и время
            Row {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "${route.distanceKm} км | ${route.durationHours} ч",
                    style = bodyTextSize,
                    color = interfaceColor
                )
            }
            // Рейтинг
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier,
                    onClick = {},
                ) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = Icons.Outlined.StarBorder,
                        contentDescription = stringResource(R.string.rating),
                        tint = interfaceColor
                    )
                }
                Text(
                    text = route.rating.toString(),
                    style = bodyTextSize,
                    color = interfaceColor
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Number of places",
                    tint = interfaceColor
                )
                // Кол-во точек
                Text(
                    text = "${route.pointsCount} точек",
                    style = bodyTextSize,
                    color = interfaceColor
                )
            }

            Text(
                modifier = Modifier.padding(8.dp),
                text = route.description,
                style = bodyTextSize,
                color = interfaceColor
            )
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            onClick = {}
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Открыть в картах",
                textAlign = TextAlign.Center
            )
        }
    }
}