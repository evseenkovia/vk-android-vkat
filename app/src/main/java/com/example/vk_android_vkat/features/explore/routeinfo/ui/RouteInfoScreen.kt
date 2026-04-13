package com.example.vk_android_vkat.features.explore.routeinfo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
//        state = RouteInfoState(isLoading = true),
//        state = RouteInfoState(error = "Что-то пошло не так((("),
        state = RouteInfoState(routeData = mockRoutes[3]),
        onEvent = {},
    )
}

@Composable
fun RouteInfoScreen(
    state: RouteInfoState,
    onEvent: (RouteInfoEvent) -> Unit
){

    val topBarUi = if (!state.isLoading && state.error.isNullOrEmpty() && state.routeData != null) {
        TopBarUiModel(
            title = state.routeData.title,
            isFavourite = state.routeData.isFavourite,
            onFavouriteToggle = { onEvent(RouteInfoEvent.ToggleFavourite) },
            onBack = { onEvent(RouteInfoEvent.BackClicked) }
        )
    } else null

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ){ innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading)
                    LoadingScreenState()
                else if (!state.error.isNullOrEmpty())
                    ErrorScreenState(state)
                else if (state.routeData != null)
                    RouteInfoLoadedScreenState(state, onEvent)
            }
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
    val route = state.routeData ?: return

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

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Изображение (позже заменится на карусель)
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f / 1f),
                    model = route.imageUrl,
                    contentDescription = "Image for route with id = ${route.id}",
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                )
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .align(Alignment.TopEnd)

                        .padding(top = 12.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,

                ) {






                        IconButton(
                            onClick = { /* TODO */ }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_comment),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }


                        IconButton(
                            onClick = { onEvent(RouteInfoEvent.ToggleFavourite) }
                        ) {
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
                    Text(
                        text = route.title,
                        style = titleTextSize,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${route.distanceKm} км | ${route.durationHours} ч",
                        style = bodyTextSize,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.StarBorder,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(iconSize)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = route.rating.toString(),
                            color = Color.White,
                            style = bodyTextSize
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(iconSize)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${route.pointsCount} точек",
                            color = Color.White,
                            style = bodyTextSize
                        )
                    }
                }
            }

            // Контент под изображением
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Название маршрута
                /*Text(
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
                }*/

                Spacer(modifier = Modifier.height(16.dp))

                // Описание
                Text(
                    text = route.description,
                    style = bodyTextSize,
                    color = interfaceColor,
                    textAlign = TextAlign.Justify
                )

                // Добавляем отступ внизу, чтобы контент не скрывался за FAB
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
                .background(
                    color = MaterialTheme.colorScheme.primary,

                )


        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.background
            )
        }

        // Кнопка "Открыть в картах" всегда внизу
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(0.9f),
            onClick = {},
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Text(
                text = "Открыть в картах",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

