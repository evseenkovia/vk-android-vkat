package com.example.vk_android_vkat.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.vk_android_vkat.R

data class TabItem(
    val route: Any,
    val iconRes: Int,
    @StringRes val labelRes: Int
) {
    @Composable
    fun IconComposable() {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = stringResource(labelRes)
        )
    }
}

// Список всех табов
val bottomTabs = listOf(
    TabItem(Search, R.drawable.ic_explore_24dp, R.string.title_explore),
    TabItem(Favourite, R.drawable.ic_bookmark_24dp, R.string.title_favourite),
    TabItem(Editor, R.drawable.ic_add_24dp, R.string.title_add),
    TabItem(Map, R.drawable.ic_map_24dp, R.string.title_map),
    TabItem(Profile, R.drawable.ic_person_24dp, R.string.title_profile)
)
