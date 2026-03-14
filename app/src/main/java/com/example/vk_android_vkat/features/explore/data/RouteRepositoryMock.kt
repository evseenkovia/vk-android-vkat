package com.example.vk_android_vkat.features.explore.data

import com.example.vk_android_vkat.data.delayTime
import com.example.vk_android_vkat.data.mockRoutes
import com.example.vk_android_vkat.features.explore.domain.RouteModel
import com.example.vk_android_vkat.features.explore.domain.RouteRepository
import com.example.vk_android_vkat.features.explore.domain.filter.RouteFilter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import okhttp3.Route
import kotlin.collections.filter

class RouteRepositoryMock : RouteRepository {

    private val routes: MutableList<RouteModel> = mockRoutes.toMutableList()
    override suspend fun getRouteById(id: Long): Result<RouteModel> {
        delay(delayTime)
        return routes.find { it.id == id }
            ?.let { Result.success(it) }
            ?: Result.failure(Exception("Маршрут с id = $id не найден" ))
    }

    override suspend fun getRouteByFilter(filter: RouteFilter): Result<List<RouteModel>> {
        delay(delayTime)
        val filteredRoutes = routes.filter {
            it.rating >= filter.rating.start && it.rating <= filter.rating.endInclusive &&
            it.durationHours >= filter.duration.start && it.durationHours <= filter.duration.endInclusive &&
            it.distanceKm >= filter.distance.start && it.distanceKm <= filter.distance.endInclusive
            //todo -> дописать еще фильтры
        }
        return if (filteredRoutes.isNotEmpty())
            Result.success(filteredRoutes)
        else
            Result.failure(Exception("По заданным фильтрам результаты не найдены"))
    }

    override suspend fun findRouteByQuery(query: String?): Result<List<RouteModel>> {
        delay(delayTime)
        return if (query.isNullOrBlank()) {
            // Пустой запрос - показываем все маршруты
            Result.success(routes)
        } else {
            // Непустой запрос - фильтруем
            val filteredRoutes = routes.filter {
                it.title.lowercase().startsWith(query.lowercase())
            }

            if (filteredRoutes.isNotEmpty()) {
                Result.success(filteredRoutes)
            } else {
                Result.failure(Exception("Результаты не найдены"))
            }
        }
    }

    override suspend fun getRoutes(): List<RouteModel> {
        delay(delayTime)
        return routes.toList()
    }

}