package com.example.vk_android_vkat.features.explore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.features.explore.domain.RouteModel
import com.example.vk_android_vkat.data.mockRoutes
import org.koin.core.parameter.parametersOf


@Preview(showBackground = true)
@Composable
fun PreviewCard(){
    RouteCard(route = mockRoutes[0])
}

@Composable
fun RouteCard(route: RouteModel, onClick: () -> Unit = {}) {
    Card(modifier = Modifier.clickable { onClick() }) {
        Column(modifier = Modifier.padding(8.dp)) {
            // ... изображение, заголовок, описание ...
            Text("Точек: ${route.pointsCount}")
            if (route.tags.isNotEmpty()) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    route.tags.forEach { tag ->
                        AssistChip(
                            onClick = { /* действие при клике, если нужно */ },
                            label = { Text(tag) }
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
) {
    val iconSize = 28.dp
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        onClick = { onClick() },
        modifier = Modifier
            .aspectRatio(2f / 3f)
            .padding(2.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
            if (!route.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = route.imageUrl,
                    contentDescription = route.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
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
            // Кнопка избранного в правом верхнем углу
            IconButton(
                onClick = onFavouriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = if (route.isFavourite)
                        Icons.Filled.Bookmark
                    else
                        Icons.Outlined.BookmarkBorder,
                    contentDescription = stringResource(R.string.title_favourite),
                    tint = Color.White
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = route.title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondary,
                lineHeight = 14.sp

            )
            Text(
                text = "${route.distanceKm} км | ${route.durationHours} ч",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Теги (горизонтальный скролл)
            if (route.tags.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(route.tags) { tag ->
                        AssistChip(
                            onClick = { },
                            label = { Text(tag, fontSize = 10.sp) },
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Рейтинг
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        onClick = {},
                        modifier = Modifier.padding(horizontal = 0.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(iconSize),
                            imageVector = Icons.Outlined.StarBorder,
                            contentDescription = stringResource(R.string.rating),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = route.rating.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
                // Количество точек
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Number of places",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${route.pointsCount} точек",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}