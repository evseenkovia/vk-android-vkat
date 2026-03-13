package com.example.vk_android_vkat.features.explore.domain.filter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.AreaChart
import androidx.compose.material.icons.outlined.BeachAccess
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Forest
import androidx.compose.material.icons.outlined.Liquor
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material.icons.outlined.PedalBike
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Terrain
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

data class RouteFilter(
    val rating: ClosedFloatingPointRange<Float> = 0f..5f,
    val distance: ClosedFloatingPointRange<Float> = 0f..50f,
    val duration: ClosedFloatingPointRange<Float> = 0f..8f,
    val routePoints: ClosedRange<Int> = 0..50,
    val sortBy: SortOption = SortOption.RATING,
    val terrainType: Terrain = Terrain.MIXED,
    val timeOfDay: Set<TimeOfDay> = emptySet(),
    val eventType: Set<EventType> = emptySet()
)

enum class EventType(val displayName: String, val icon: ImageVector) {
    WALKING("Прогулка", Icons.AutoMirrored.Outlined.DirectionsWalk),
    CYCLING("Велопрогулка", Icons.Outlined.PedalBike),
    PHOTOSESSION("Фотосессия", Icons.Outlined.PhotoCamera),
    PUBS("Бары", Icons.Outlined.Liquor),
    ENTERTAINMENT("Развлечения", Icons.Outlined.TheaterComedy),
    ROMANTIC("Свидание", Icons.Outlined.FavoriteBorder)
}

enum class TimeOfDay(val displayName: String, val icon: ImageVector) {
    MORNING("Утро", Icons.Outlined.WbSunny),
    DAY("День", Icons.Outlined.BeachAccess),
    EVENING("Вечер", Icons.Outlined.Nightlight),
    NIGHT("Ночь", Icons.Outlined.Bedtime)
}

enum class Terrain(val displayName: String, val icon: ImageVector) {
    FOREST("Лесная", Icons.Outlined.Forest),
    URBAN("Городская", Icons.Outlined.LocationCity),
    MOUNTAIN("Горная", Icons.Outlined.Terrain),
    WATER("У воды", Icons.Outlined.WaterDrop),
    MIXED("Смешанная", Icons.Outlined.AreaChart)
}

enum class SortOption {
    RATING,            // по рейтингу
    DURATION_ASC,      // по длительности (сначала короткие)
    DURATION_DESC,     // по длительности (сначала длинные)
    DISTANCE_ASC,      // по расстоянию (близкие)
    DISTANCE_DESC,     // по расстоянию (дальние)
}

