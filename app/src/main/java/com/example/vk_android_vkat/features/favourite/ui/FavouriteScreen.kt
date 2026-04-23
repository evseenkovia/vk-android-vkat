package com.example.vk_android_vkat.features.favourite.ui

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults.contentWindowInsets
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.common.theme.AppLightColorScheme
import com.example.vk_android_vkat.common.theme.AppTypography
import com.example.vk_android_vkat.common.theme.PrimaryButton
import com.example.vk_android_vkat.common.theme.SearchField
import com.example.vk_android_vkat.features.explore.domain.RouteModel
import com.example.vk_android_vkat.features.explore.routeinfo.ui.filter.FilterDialog
import com.example.vk_android_vkat.features.explore.ui.ExploreEvent
import com.example.vk_android_vkat.features.explore.ui.ExploreScreen
import com.example.vk_android_vkat.features.explore.ui.ExploreState
import com.example.vk_android_vkat.features.explore.ui.RouteCard
import com.example.vk_android_vkat.features.explore.ui.RoutesList
import kotlinx.coroutines.launch

@Composable
fun FavouriteScreen(
    state: ExploreState,
    onEvent: (ExploreEvent) -> Unit,
    onRouteClick: (Int) -> Unit = {},
    onEnter: () -> Unit
) {

    LaunchedEffect(Unit) {
        onEnter()
    }

    var showFilterDialog by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    val tabs = listOf("Избранное", "Мои маршруты")

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            onEvent(ExploreEvent.Retry({ isRefreshing = false }))
        }
    ) {
        Scaffold(
            modifier = Modifier.statusBarsPadding(),
            topBar = {
                Column {
                    ExploreTopBar(
                        query = state.searchQuery,
                        onQueryChange = { onEvent(ExploreEvent.QueryChanged(it)) },
                        onFilterClick = { showFilterDialog = true }
                    )

                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                text = {
                                    Text(
                                        text = title,
                                        fontSize = 16.sp,
                                        color = if (pagerState.currentPage == index)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            )
                        }
                    }
                    HorizontalDivider(thickness = 1.dp)
                }
            },
            content = { innerPadding ->
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) { page ->
                    when (page) {
                        0 -> RoutesList(
                            routes = state.routeList,
                            onClick = onRouteClick,
                            onFavouriteClick = { routeID ->
                                onEvent(ExploreEvent.ToggleFavourite(routeID))
                            }
                        )

                        1 -> RoutesList(
                            routes = state.routeList,
                            onClick = onRouteClick,
                            onFavouriteClick = { routeID ->
                                onEvent(ExploreEvent.ToggleFavourite(routeID))
                            }
                        )
                    }
                }
            },
            contentWindowInsets = WindowInsets(0.dp)
        )

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
                        tint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(R.drawable.ic_map_pin_24dp),
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
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background),

            )
    }
}

@Composable
fun LoadingState() = Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) { CircularProgressIndicator() }

@Composable
fun ErrorState(
    state: ExploreState,
    onRetry: () -> Unit
){
    val color = if (state.isFavourite)
        AppLightColorScheme.onSurface
    else
        AppLightColorScheme.error
    Box(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!state.isFavourite){
                Icon(
                    modifier = Modifier.size(128.dp),
                    imageVector = Icons.Outlined.ErrorOutline,
                    contentDescription = "Error",
                    tint = color,
                )
                Text(
                    text = "Ошибка",
                    style = AppTypography.headlineLarge,
                    color = color
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            Text(
                text = state.error.toString(),
                color = color,
                style = AppTypography.titleLarge
            )
            if (!state.isFavourite){
                Spacer(Modifier.height(8.dp))
                PrimaryButton(
                    text = stringResource(R.string.try_again),
                    onClick = onRetry
                )
            }
        }
    }
}

@Composable
fun RoutesList(
    routes: List<RouteModel>,
    onClick: (Int) -> Unit,
    onFavouriteClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp),
        columns = GridCells.Fixed(2),
        content = {
            items(routes, key = { it.id }) { route ->
                RouteCard(
                    route = route,
                    onClick = { onClick(route.id) },
                    onFavouriteClick = { onFavouriteClick(route.id)}
                )
            }
        }
    )
}

