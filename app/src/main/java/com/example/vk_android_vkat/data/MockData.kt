package com.example.vk_android_vkat.data

import com.example.vk_android_vkat.domain.model.RouteUi

val mockRoutes = listOf(
    RouteUi(
        id = 1,
        title = "Прогулка по старому городу",
        description = "Исторический маршрут по центральной части города",
        distanceKm = 10,
        durationHours = 2,
        pointsCount = 7,
        rating = 4.7f,
        imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTEHZLEG4vE90aZYXy-YtldelQ7QHWYwwB-NA&s"
    ),
    RouteUi(
        id = 2,
        title = "Лесной маршрут",
        description = "Приятная прогулка по лесной местности",
        distanceKm = 15,
        durationHours = 4,
        pointsCount = 12,
        rating = 4.9f,
        imageUrl = "https://static.wikia.nocookie.net/darksouls/images/1/19/Поселок_Олачиль_1.jpg/revision/latest/smart/width/386/height/259?cb=20160130220340&path-prefix=ru"
    ),
    RouteUi(
        id = 3,
        title = "На берегу озера",
        description = "Маршрут вдоль живописного озера",
        distanceKm = 8,
        durationHours = 1,
        pointsCount = 5,
        rating = 4.5f,
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/7d/Lake_view.jpg"
    ),
    RouteUi(
        id = 4,
        title = "Горный перевал",
        description = "Сложный маршрут через горные тропы",
        distanceKm = 20,
        durationHours = 6,
        pointsCount = 10,
        rating = 4.8f,
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/3/3b/Mountain_pass.jpg"
    ),
    RouteUi(
        id = 5,
        title = "Парк развлечений",
        description = "Прогулка с посещением достопримечательностей и кафе",
        distanceKm = 5,
        durationHours = 2,
        pointsCount = 8,
        rating = 4.3f,
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/2/25/Amusement_Park.jpg"
    ),
    RouteUi(
        id = 6,
        title = "Вдоль реки",
        description = "Маршрут с живописными видами на реку и мосты",
        distanceKm = 12,
        durationHours = 3,
        pointsCount = 9,
        rating = 4.6f,
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/6/61/Riverside_path.jpg"
    )
)
