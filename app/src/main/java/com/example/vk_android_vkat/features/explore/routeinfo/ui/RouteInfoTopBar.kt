package com.example.vk_android_vkat.features.explore.routeinfo.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.vk_android_vkat.features.explore.routeinfo.ui.TopBarUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun RouteInfoTopBar(
    uiModel: TopBarUiModel
){
    TopAppBar(
        title = { Text(uiModel.title) },
        navigationIcon = {
            IconButton(
                onClick = { uiModel.onBack?.invoke() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Оставить отзыв */ }) {
                Icon(
                    imageVector = Icons.Outlined.AddComment,
                    contentDescription = "Add comment"
                )
            }
            IconButton(onClick = { uiModel.onFavouriteToggle?.invoke() }) {
                Icon(
                    imageVector = if (uiModel.isFavourite)
                        Icons.Filled.Bookmark
                    else
                        Icons.Outlined.BookmarkBorder,
                    contentDescription = "Add to favourite"
                )
            }
        }
    )
}