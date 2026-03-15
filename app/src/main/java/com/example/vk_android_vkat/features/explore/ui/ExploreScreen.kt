package com.example.vk_android_vkat.features.explore.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.ErrorOutline
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import com.example.vk_android_vkat.common.theme.AppTypography
import com.example.vk_android_vkat.common.theme.PrimaryButton
import com.example.vk_android_vkat.common.theme.SearchField
import com.example.vk_android_vkat.features.explore.domain.RouteModel
import com.example.vk_android_vkat.features.explore.routeinfo.ui.filter.FilterDialog
import com.example.vk_android_vkat.features.navigation.Explore

@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview() {
    ExploreScreen(
        state = ExploreState(
            isLoading = false,
            error = "ОшибкаОшибкаОшибкаОшибкаОшибкаОшибкаОшибкаОшибкаОшибка"
        ),
        onEvent = {},
        onRouteClick = {}
    )
}

@Composable
fun ExploreScreen(
    state: ExploreState,
    onEvent: (ExploreEvent) -> Unit,
    onRouteClick: (Long) -> Unit = {}
) {
    // Состояние для диалога фильтров
    var showFilterDialog by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            onEvent(ExploreEvent.Retry({ isRefreshing = false }))
        }
    ) {
        Scaffold(
            modifier = Modifier.statusBarsPadding(),
            topBar = {
                ExploreTopBar(
                    query = state.searchQuery,
                    onQueryChange = { onEvent(ExploreEvent.QueryChanged(it)) },
                    onFilterClick = { showFilterDialog = true }
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (state.isLoading)
                        LoadingState()
                    else if (!state.error.isNullOrBlank()) {
                        ErrorState(
                            errorMessage = state.error,
                            onRetry = { onEvent(ExploreEvent.Retry({ isRefreshing = false })) }
                        )
                    }
                    RoutesList(routes = state.routeList, onClick = onRouteClick)
                }
            },
            contentWindowInsets = WindowInsets(0.dp)
        )

        // Диалог фильтров
        FilterDialog(
            showDialog = showFilterDialog,
            onDismiss = { showFilterDialog = false },
            filters = state.filters,
            onFiltersChanged = { newFilters ->
                onEvent(ExploreEvent.FiltersChanged(newFilters))
            },
            onApply = { onEvent(ExploreEvent.ApplyFilters) },
            onClearFilters = { onEvent(ExploreEvent.ClearFilters) }
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ExploreTopBarPreview() {
//    ExploreTopBar(
//        query = "",
//        onQueryChange = {},
//    ) {}
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        TopAppBar(
            title = {
                SearchField(query = query, onQueryChange = onQueryChange)
            },
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
                    onClick = onFilterClick
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FilterAlt,
                        contentDescription = "Menu"
                    )
                }
            },
        )
        // Поле текстового поиска


//        SearchField(
//            modifier = Modifier
//                .padding(horizontal = 56.dp)
//                .align(Alignment.Center),
//            shape = RoundedCornerShape(32.dp),
//            value = query,
//            onValueChange = onQueryChange,
//            onClearClick = { onQueryChange("") },
//            placeholderText = "Поиск..."
//        )
    }
}

@Composable
fun ExploreTopBar2() {
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
fun ErrorState(
    errorMessage: String,
    onRetry: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(128.dp),
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = "Error",
                tint = Color.Red,
            )
            Text(
                text = "Ошибка",
                style = AppTypography.headlineLarge,
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = errorMessage,
                color = Color.Red,
                style = AppTypography.titleLarge
            )
            Spacer(Modifier.height(8.dp))
            PrimaryButton(
                text = stringResource(R.string.try_again),
                onClick = onRetry
            )
        }
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
