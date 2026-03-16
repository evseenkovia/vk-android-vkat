package com.example.vk_android_vkat.features.explore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.domain.model.RouteModel
import com.example.vk_android_vkat.data.mockRoutes


@Preview(showBackground = true)
@Composable
fun PreviewCard(){
    RouteCard(route = mockRoutes[0])
}

@Composable
fun RouteCard(
    route: RouteModel,
    onClick: () -> Unit = {},
    onFavouriteClick: () -> Unit = {}
){
    ElevatedCard(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .padding(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxWidth()
            ) {
                if (!route.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = route.imageUrl,
                        contentDescription = route.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        error = rememberVectorPainter(Icons.Filled.Image),
                        placeholder = rememberVectorPainter(Icons.Filled.Image),
                    )
                } else {
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

                // Кнопка избранного на изображении
                IconButton(
                    onClick = { onFavouriteClick() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = if (route.isFavourite)
                            Icons.Filled.Bookmark
                        else
                            Icons.Outlined.BookmarkBorder,
                        contentDescription = stringResource(R.string.title_favourite),
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Нижняя часть с информацией (занимает 40% карточки)
            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Название маршрута
                Text(
                    text = route.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                )

                // Дистанция и время
                Text(
                    text = "${route.distanceKm} км · ${route.durationHours} ч",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Рейтинг и количество точек в одной строке
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Рейтинг
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = stringResource(R.string.rating),
                            tint = Color(0xFFFFC107), // Желтый цвет для звезды
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", route.rating),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }

                    // Количество точек
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Number of places",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${route.pointsCount} точек",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RouteCard2(
    route: RouteModel,
    onClick: () -> Unit = {},
    onFavouriteClick: () -> Unit = {}
){
    val iconSize = 24.dp
    ElevatedCard(
        onClick = { onClick() },
        modifier = Modifier
            .aspectRatio(2f / 3f)
            .padding(2.dp),
    ){
        if (!route.imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = route.imageUrl,
                contentDescription = route.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .aspectRatio(1f),
                error = rememberVectorPainter(Icons.Filled.Image),
                placeholder = rememberVectorPainter(Icons.Filled.Image),
            )
        } else {
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp) // Уменьшил паддинг
        ) {
            // Название маршрута
            Text(
                text = route.title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Дистанция и время
            Text(
                text = "${route.distanceKm} км | ${route.durationHours} ч",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Рейтинг, точки и избранное
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Рейтинг
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {},
                        modifier = Modifier.padding(horizontal = 0.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {

                        Icon(
                            modifier = Modifier.size(iconSize),
                            imageVector = Icons.Outlined.StarBorder,
                            contentDescription = stringResource(R.string.rating),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = route.rating.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Количество точек
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Number of places",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${route.pointsCount} точек",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Кнопка избранного
                IconButton(
                    onClick = { onFavouriteClick() }
                ) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = if (route.isFavourite)
                            Icons.Filled.Bookmark
                        else
                            Icons.Outlined.BookmarkBorder,
                        contentDescription = stringResource(R.string.title_favourite),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}