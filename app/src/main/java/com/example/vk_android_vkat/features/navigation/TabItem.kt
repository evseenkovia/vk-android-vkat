package com.example.vk_android_vkat.features.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vk_android_vkat.R

data class TabItem(
    val graphRoute: Any,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val labelRes: Int
) {
    @Composable
    fun IconComposable(isSelected: Boolean) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = stringResource(labelRes)
        )
    }
}

// Список всех табов
val bottomNavDestinations = listOf(
    TabItem(
        ExploreGraph,
        Icons.Filled.Explore,
        Icons.Outlined.Explore,
        R.string.title_explore),
    TabItem(
        FavouriteGraph,
        Icons.Filled.Bookmark,
        Icons.Outlined.BookmarkBorder,
        R.string.title_favourite),
    TabItem(
        graphRoute = EditorGraph,
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Outlined.Add,
        labelRes = R.string.title_add
    ),
    TabItem(
        graphRoute = MapGraph,
        selectedIcon = Icons.Filled.Map,
        unselectedIcon = Icons.Outlined.Map,
        labelRes = R.string.title_map
    ),
    TabItem(
        graphRoute = ProfileGraph,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        labelRes = R.string.title_profile
    )
)
