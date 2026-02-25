package com.example.vk_android_vkat.data

import com.example.vk_android_vkat.domain.model.RouteModel
import com.example.vk_android_vkat.features.profile.ProfileContentUi
import com.example.vk_android_vkat.features.profile.ProfileHeaderUi
import com.example.vk_android_vkat.features.profile.ProfileItemUi
import com.example.vk_android_vkat.features.profile.ProfileSection
import com.example.vk_android_vkat.features.profile.ProfileUiState

// Список маршрутов
val mockRoutes = listOf(
    RouteModel(
        id = 1,
        title = "Прогулка по старому городу",
        description = "Исторический маршрут по центральной части города",
        distanceKm = 10,
        durationHours = 2,
        pointsCount = 7,
        rating = 4.7f,
        imageUrl = "https://7d9e88a8-f178-4098-bea5-48d960920605.selcdn.net/7cdfe127-c49b-4d4b-9fec-78b998cc76f0/"
    ),
    RouteModel(
        id = 2,
        title = "Лесной маршрут",
        description = "Приятная прогулка по лесной местности",
        distanceKm = 15,
        durationHours = 4,
        pointsCount = 12,
        rating = 4.9f,
        imageUrl = "https://resize.tripster.ru/xLIeXLlZi1JYd0RY9ySRMkXqZAU=/fit-in/1600x900/filters:no_upscale()/https://cdn.tripster.ru/photos/0012df63-66d0-466b-afbd-9b3912170313.jpg?width=1200&height=630"
    ),
    RouteModel(
        id = 3,
        title = "На берегу озера",
        description = "Маршрут вдоль живописного озера",
        distanceKm = 8,
        durationHours = 1,
        pointsCount = 5,
        rating = 4.5f,
        imageUrl = "https://moya-planeta.ru/upload/images/xl/c7/67/c767f4b9bb0b1f2f130adce886abd98003c9249a.jpg"
    ),
    RouteModel(
        id = 4,
        title = "Горный перевал",
        description = "Сложный маршрут через горные тропы",
        distanceKm = 20,
        durationHours = 6,
        pointsCount = 10,
        rating = 4.8f,
        imageUrl = "https://thumbs.dreamstime.com/b/%D0%BC%D0%BE%D1%82%D0%BE%D1%86%D0%B8%D0%BA-%D0%B8%D1%81%D1%82-%D0%BD%D0%B0-%D0%BE%D1%80%D0%BE%D0%B3%D0%B5-%D0%BF%D0%B5%D1%80%D0%B5%D0%B2%D0%B0-%D0%B0-%D0%B2-%D0%B0-%D1%8C%D0%BF%D0%B0%D1%85-68572879.jpg"
    ),
    RouteModel(
        id = 5,
        title = "Парк развлечений",
        description = "Прогулка с посещением достопримечательностей и кафе",
        distanceKm = 5,
        durationHours = 2,
        pointsCount = 8,
        rating = 4.3f,
        imageUrl = "https://kudagid.ru/images/razvlechenia/attrakcion.jpg"
    ),
    RouteModel(
        id = 6,
        title = "Вдоль реки",
        description = "Маршрут с живописными видами на реку и мосты",
        distanceKm = 12,
        durationHours = 3,
        pointsCount = 9,
        rating = 4.6f,
        imageUrl = "https://eco-trails.ru/catalog/moskva/chermyanka/img/3.jpg"
    )
)

// Данные для профиля
val mockProfile: ProfileUiState.Content = ProfileUiState.Content(
    data = ProfileContentUi(
        header = ProfileHeaderUi(
            userName = "Алексей",
            email = "alex@example.com",
            avatarUrl = "https://i.pravatar.cc/301"
        ),
        sections = listOf(
            ProfileItemUi.Switch(title = "Уведомления", checked = true),
            ProfileItemUi.Switch(title = "Тёмная тема", checked = false),
            ProfileItemUi.Info(title = "Email", subtitle = "alex@example.com"),
            ProfileItemUi.Navigation(title = "Изменить пароль", section = ProfileSection.Account),
            ProfileItemUi.Navigation(title = "Конфиденциальность", section = ProfileSection.Privacy)
        )
    )
)

//Данные для авторизации
const val mockEmail = "alex@example.com"
const val mockPassword = "12345678"

//Задержка-имитация API (в мс)
const val delayTime = 800L
