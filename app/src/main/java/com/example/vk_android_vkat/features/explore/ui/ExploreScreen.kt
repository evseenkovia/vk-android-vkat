package com.example.vk_android_vkat.features.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.common.theme.SearchField
import com.example.vk_android_vkat.domain.model.RouteModel

//@Preview(showBackground = true)
//@Composable
//fun ExploreScreenPreview(){
//    ExploreScreen(
//        state = ExploreState.Loading,
//        onEvent = {},
//        onRouteClick = {}
//    )
//}

@Composable
fun ExploreScreen(
    state: ExploreState,
    onEvent: (ExploreEvent) -> Unit,
    onRouteClick: (Long) -> Unit = {}
) {
    Scaffold(
        topBar = {
            ExploreTopBar()
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (state) {
                    is ExploreState.Loading -> LoadingState()
                    is ExploreState.Error -> ErrorState(
                        errorMessage = state.message, onRetry = { onEvent(ExploreEvent.Retry) }
                    )

                    is ExploreState.Routes -> {
                        if (state.data.isEmpty())
                            EmptyState()
                        else
                            RoutesList(routes = state.data, onClick = onRouteClick)
                    }

                    else -> {}
                }
            }
        },
        contentWindowInsets = WindowInsets(0.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ExploreTopBarPreview() {
    ExploreTopBar(

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreTopBar(
    onSearchQueryChange: (String) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        TopAppBar(

            title = {},
            navigationIcon = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onSurface,
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Locations nearby"
                    )
                }
            },
            actions = {

                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FilterAlt,
                        contentDescription = "Menu"
                    )
                }
            },
        )

        var searchQuery by remember { mutableStateOf("") }
        SearchField(
            modifier = Modifier
                .padding(horizontal = 56.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(32.dp),
            value = searchQuery,
            onValueChange = { searchQuery = it },
            onClearClick = { }
        )
    }
}

@Composable
fun ExploreTopBar() {
    LazyRow(
        modifier = Modifier
            .padding(8.dp)
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            AssistChip(
                onClick = { },
                label = { Text(stringResource(R.string.nearby)) },
                leadingIcon = {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                }
            )
        }
        item { AssistChip(onClick = { }, label = { Text(stringResource(R.string.target)) }) }
        item { AssistChip(onClick = { }, label = { Text(stringResource(R.string.location)) }) }
        item { AssistChip(onClick = { }, label = { Text(stringResource(R.string.day_time)) }) }
        item { AssistChip(onClick = { }, label = { Text(stringResource(R.string.duration)) }) }
    }
}

@Composable
fun LoadingState() = Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) { CircularProgressIndicator() }

@Composable
fun ErrorState(errorMessage: String, onRetry: () -> Unit) = Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(errorMessage, color = Color.Red)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) { Text(stringResource(R.string.try_again)) }
    }
}

@Composable
fun EmptyState() = Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) { Text(stringResource(R.string.no_routes)) }


@Composable
fun RoutesList(routes: List<RouteModel>, onClick: (Long) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp),
        columns = GridCells.Fixed(2),
        content = {
            items(routes, key = { it.id }) { route ->
                RouteCard(
                    route = route,
                    onClick = { onClick(route.id) }) // RouteCard можно расширять
            }
        }

    )
}
