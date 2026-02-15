package com.example.vk_android_vkat.ui.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.domain.model.RouteModel
import com.example.vk_android_vkat.data.mockRoutes


@Preview(showBackground = true)
@Composable
fun PreviewCard(){
    RouteCard(
        route = mockRoutes[0],
        )
}


@Composable
fun RouteCard(
    route: RouteModel,
    onClick: () -> Unit = {},
    onFavouriteClick: () -> Unit = {}
){
    val interfaceColor = Color.White
    Card(
        modifier = Modifier
            .clickable(onClick = { onClick() })
            .aspectRatio(1f)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
    ){
        Box(modifier = Modifier.fillMaxSize()) {
            // Фоновое изображение маршрута
            if (!route.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = route.imageUrl,
                    contentDescription = route.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    error = rememberVectorPainter(Icons.Filled.Image),
                    placeholder = rememberVectorPainter(Icons.Filled.Image)
                )
            } else {
                // Если нет картинки, показываем иконку
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = stringResource(R.string.no_image),
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            // Полупрозрачный градиент для читаемости текста
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0x80000000)),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )
            // Избранное
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                IconButton(
                    onClick = { onFavouriteClick() },
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = if (route.isFavourite)
                            Icons.Filled.Bookmark
                        else
                            Icons.Outlined.BookmarkBorder,
                        contentDescription = stringResource(R.string.title_favourite),
                        tint = interfaceColor
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    //Название маршрута
                    Text(
                        text = route.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = interfaceColor
                    )
                    // Дистанция и время
                    Row {
                        Text(
                            text = "${route.distanceKm} км | ${route.durationHours} ч",
                            style = MaterialTheme.typography.titleMedium,
                            color = interfaceColor
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Number of places",
                            tint = interfaceColor
                        )
                        // Кол-во точек
                        Text(
                            text = "${route.pointsCount} точек",
                            style = MaterialTheme.typography.titleMedium,
                            color = interfaceColor
                        )
                    }
                }
                // Рейтинг
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 4.dp,
                            bottom = 8.dp
                        ),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IconButton(
                        onClick = {},
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            imageVector = Icons.Outlined.StarBorder,
                            contentDescription = stringResource(R.string.rating),
                            tint = interfaceColor
                        )
                    }
                    Text(
                        text = route.rating.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = interfaceColor
                    )
                }
            }
        }
    }
}