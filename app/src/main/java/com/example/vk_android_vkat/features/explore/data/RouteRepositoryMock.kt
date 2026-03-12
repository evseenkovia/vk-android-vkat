package com.example.vk_android_vkat.features.explore.data

import com.example.vk_android_vkat.data.mockRoutes
import com.example.vk_android_vkat.features.explore.domain.RouteModel
import com.example.vk_android_vkat.features.explore.domain.RouteRepository

class RouteRepositoryMock : RouteRepository {

    private val routes = mockRoutes

    override suspend fun getRouteById(id: Long): Result<RouteModel> {
        return routes.find { it.id == id }
            ?.let { Result.success(it) }
            ?: Result.failure(Exception("Маршрут с id = $id не найден" ))
    }

    override suspend fun getRouteByFilter(filters: List<String>): Result<Any> {
        TODO("Сделать запрос к БД по фильтрам")
    }

    override suspend fun findRouteByQuery(query: String?): Result<List<RouteModel>> {
        return if (query.isNullOrBlank()) {
            // Пустой запрос - показываем все маршруты
            Result.success(mockRoutes)
        } else {
            // Непустой запрос - фильтруем
            val filteredRoutes = mockRoutes.filter {
                it.title.lowercase().startsWith(query.lowercase())
            }

            if (filteredRoutes.isNotEmpty()) {
                Result.success(filteredRoutes)
            } else {
                Result.failure(Exception("Результаты не найдены"))
            }
        }
    }
}