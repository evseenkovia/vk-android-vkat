package com.example.vk_android_vkat.ui.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.data.mockRoutes
import com.example.vk_android_vkat.domain.model.RouteModel

@Preview(showBackground = true)
@Composable
fun ScreenSearchPreviewLoading(){
    SearchScreen(
        uiState = SearchUiState(
            isLoading = false,
            routes = emptyList(),
            error = "Не удалось загрузить маршруты"
        ),
        onEvent = {}
    )
}

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onEvent: (SearchUiEvent) -> Unit
) {
    Scaffold(
        topBar = { ToolBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> LoadingState()
                uiState.error != null -> ErrorState(uiState.error, onRetry = { onEvent(SearchUiEvent.Retry) })
                uiState.routes.isEmpty() -> EmptyState()
                else -> RoutesList(uiState.routes) { onEvent(SearchUiEvent.RouteClicked(it)) }
            }
        }
    }
}

@Composable
fun ToolBar() {
    LazyRow(
        modifier = Modifier.
        padding(8.dp),
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
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(bottom = 56.dp)
    ) {
        items(routes, key = { it.id }) { route ->
            RouteCard(route = route, onClick = { onClick(route.id) }) // RouteCard можно расширять
        }
    }
}
