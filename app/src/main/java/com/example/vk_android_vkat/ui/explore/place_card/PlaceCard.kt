package com.example.vk_android_vkat.ui.explore.place_card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp

@Composable
fun PlaceCard(
){
    Card(
        modifier = Modifier
            .clickable(onClick = { })
            .aspectRatio(1f)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            IconButton(
                onClick = {},
            ) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = "Favourite"
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "Place name",
                    style = MaterialTheme.typography.headlineLarge
                )
                Row {
                    Text(
                        text = "${10} км | ${50} мин",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Number of places")
                    Text(
                        text = "${7} точек",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = Icons.Outlined.StarBorder,
                        contentDescription = "Favourite"
                    )
                }
                Text(
                    text = "${4.7}k",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}