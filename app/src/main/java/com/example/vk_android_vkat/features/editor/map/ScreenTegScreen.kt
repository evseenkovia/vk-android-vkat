package com.example.vk_android_vkat.features.editor.map

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.features.editor.EditorEvent
import com.example.vk_android_vkat.features.editor.EditorViewModel

data class TegItem(
    val id: String,
    val name: String
)

data class TegGroup(
    val title: String,
    val items: List<TegItem>
)

@Composable
fun ScreenTegScreen(viewModel: EditorViewModel) {
    val selectedTegs = remember { mutableStateSetOf<String>() }

    val groups = remember {
        listOf(
            TegGroup("Местность", listOf(
                TegItem("urban", "Городские"),
                TegItem("historical", "Исторические"),
                TegItem("nature", "Природные")
            )),
            TegGroup("Длительность", listOf(
                TegItem("short", "Короткий"),
                TegItem("medium", "Средний"),
                TegItem("long", "Длительный")
            )),
            TegGroup("Время суток", listOf(
                TegItem("day", "Дневной"),
                TegItem("evening", "Вечерний"),
                TegItem("anytime", "Любое время")
            )),
            TegGroup("Чем заняться", listOf(
                TegItem("walk", "Погулять"),
                TegItem("date", "На свидание"),
                TegItem("photo", "Пофоткаться")
            ))
        )
    }

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    // Вычисляем названия прямо здесь
                    val tagNames = groups.flatMap { it.items }
                        .filter { it.id in selectedTegs }
                        .map { it.name }
                        .toSet()
                    Log.d("ScreenTeg", "Selected tags: $tagNames")
                    viewModel.onEvent(EditorEvent.FinishRouteCreation(tagNames))
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Опубликовать")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.select_tegs),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            items(groups) { group ->
                TegGroupSection(
                    group = group,
                    selectedTegs = selectedTegs,
                    onTegClick = { tegId ->
                        if (selectedTegs.contains(tegId)) {
                            selectedTegs.remove(tegId)
                        } else {
                            selectedTegs.add(tegId)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TegGroupSection(
    group: TegGroup,
    selectedTegs: Set<String>,
    onTegClick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = group.title, style = MaterialTheme.typography.titleMedium)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            group.items.forEach { teg ->
                val isSelected = selectedTegs.contains(teg.id)
                FilterChip(
                    selected = isSelected,
                    onClick = { onTegClick(teg.id) },
                    label = { Text(teg.name) }
                )
            }
        }
    }
}