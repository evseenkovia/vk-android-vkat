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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.vk_android_vkat.R

data class TabItem(
    val graphRoute: Any,
    val selectedIcon: @Composable (Modifier) -> Unit,
    val unselectedIcon: @Composable (Modifier) -> Unit,
    @StringRes val labelRes: Int
) {
    @Composable
    fun IconComposable(isSelected: Boolean, modifier: Modifier = Modifier) {
        if (isSelected) selectedIcon(modifier) else unselectedIcon(modifier)
    }
}

// Список всех табов
val bottomNavDestinations = listOf(
    TabItem(
        ExploreGraph,
        selectedIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_explore_30dp),
                contentDescription = null
            )
        },
        unselectedIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_explore_30dp),
                contentDescription = null
            )
        },
        labelRes = R.string.title_explore
    ),
    TabItem(
        FavouriteGraph,
        selectedIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_bookmark_32dp),
                contentDescription = null
            )
        },
        unselectedIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_bookmark_32dp),
                contentDescription = null
            )
        },
        R.string.title_favourite),
    TabItem(
        graphRoute = EditorGraph,
        selectedIcon = {
            Icon(
                Icons.Filled.Add,
                contentDescription = null
            )
        },
        unselectedIcon = {
            Icon(
                Icons.Outlined.Add,
                contentDescription = null
            )
        },
        labelRes = R.string.title_add
    ),
    TabItem(
        graphRoute = MapGraph,
        selectedIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_map_32dp),
                contentDescription = null
            )
        },
        unselectedIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_map_32dp),
                contentDescription = null
            )
        },
        labelRes = R.string.title_map
    ),
    TabItem(
        graphRoute = ProfileGraph,
        selectedIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_user_32dp),
                contentDescription = null
            )
        },
        unselectedIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_user_32dp),
                contentDescription = null
            )
        },
        labelRes = R.string.title_profile
    )
)
