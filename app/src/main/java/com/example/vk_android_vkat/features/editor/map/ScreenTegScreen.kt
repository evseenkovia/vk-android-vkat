package com.example.vk_android_vkat.features.editor.map

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vk_android_vkat.R
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable

// Модель данных для тега
data class TegItem(
    val id: String,
    val name: String
)

// Группа тегов с заголовком
data class TegGroup(
    val title: String,
    val items: List<TegItem>
)

@Composable
fun ScreenTegScreen() {
    // Состояние выбранных тегов (по id)
    val selectedTegs = remember { mutableStateSetOf<String>() }

    // Группы тегов (можно вынести в ресурсы или ViewModel)
    val groups = remember {
        listOf(
            TegGroup(
                title = "Местность",
                items = listOf(
                    TegItem("urban", "Городские"),
                    TegItem("historical", "Исторические"),
                    TegItem("nature", "Природные")
                )
            ),
            TegGroup(
                title = "Длительность",
                items = listOf(
                    TegItem("short", "Короткий"),
                    TegItem("medium", "Средний"),
                    TegItem("long", "Длительный")
                )
            ),
            TegGroup(
                title = "Время суток",
                items = listOf(
                    TegItem("day", "Дневной"),
                    TegItem("evening", "Вечерний"),
                    TegItem("anytime", "Любое время")
                )
            ),
            TegGroup(
                title = "Чем заняться",
                items = listOf(
                    TegItem("walk", "Погулять"),
                    TegItem("date", "На свидание"),
                    TegItem("photo", "Пофоткаться")
                )
            )
        )
    }

    Scaffold(
        bottomBar = {
            // Кнопка внизу (пока без действия)
            Button(
                onClick = { /* пока ничего не делаем */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(stringResource(R.string.continue_text)) // "Опубликовать"
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

            // Перебираем группы
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
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = group.title,
            style = MaterialTheme.typography.titleMedium
        )

        // Чипы в горизонтальном ряду с переносом
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            group.items.forEach { teg ->
                val isSelected = selectedTegs.contains(teg.id)
                FilterChip(
                    selected = isSelected,
                    onClick = { onTegClick(teg.id) },
                    label = { Text(teg.name) },
                    modifier = Modifier
                )
            }
        }
    }
}

// Вспомогательный компонент для переноса чипов (можно заменить на Row + wrap)
@Composable
fun TegFlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        content() // вызов контента внутри лямбды FlowRow
    }
}