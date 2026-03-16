package com.example.vk_android_vkat.features.explore.data

import android.util.Log
import androidx.core.graphics.rotationMatrix
import com.example.vk_android_vkat.features.explore.data.local.LocalDataSource
import com.example.vk_android_vkat.features.explore.data.local.LocalDataSourceMock
import com.example.vk_android_vkat.features.explore.data.remote.RemoteDataSource
import com.example.vk_android_vkat.features.explore.data.remote.RemoteDataSourceMock
import com.example.vk_android_vkat.features.explore.domain.RouteModel
import com.example.vk_android_vkat.features.explore.domain.RouteRepository
import com.example.vk_android_vkat.features.explore.domain.filter.RouteFilter
import kotlinx.coroutines.flow.first

class RouteRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : RouteRepository {
    init {
        Log.d("RouteRepositoryImpl", "Repository CREATED: $this")
    }
    override suspend fun getRouteById(id: Long): Result<RouteModel> {
        // сначала ищем в favourites
        localDataSource.getFavouriteById(id)?.let { return Result.success(it) }

        // если нет в favourites, берём с remote
        val route = remoteDataSource.getRouteById(id)
            ?: return Result.failure(Exception("Маршрут $id не найден"))

        return Result.success(route.copy(isFavourite = false))
    }

    override suspend fun getRoutes(): Result<List<RouteModel>> {
        return try {
            val remoteRoutes = remoteDataSource.getRoutes()
            val favourites = localDataSource.getFavouriteRoutes().associateBy { it.id }

            val mergedRoutes = remoteRoutes.map { route ->
                favourites[route.id] ?: route.copy(isFavourite = false)
            }

            Result.success(mergedRoutes)
        } catch (e: Exception) {
            // Fallback: возвращаем только избранное при отсутствии сети
            val results = localDataSource.getFavouriteRoutes()
            if (results.isNotEmpty()) {
                Result.success(results)
            } else {
                Result.failure(Exception("Ошибка при получении Маршрутов"))
            }
        }
    }

    override suspend fun findRouteByQuery(query: String?): Result<List<RouteModel>> {
        val allRoutes = getRoutes() // уже учитывает favourites
        if (allRoutes.isFailure) return allRoutes
        val routes = allRoutes.getOrNull() ?: return Result.failure(Exception("Не удалось получить маршруты"))
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

    override suspend fun getRouteByFilter(filter: RouteFilter): Result<List<RouteModel>> {
        val allRoutes = getRoutes()
        if (allRoutes.isFailure) return allRoutes
        val routes = allRoutes.getOrNull() ?: return Result.failure(Exception("Не удалось получить маршруты"))
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

    suspend fun toggleFavourite(route: RouteModel) {
        //TODO: может быть переработать в зависимости от того как localDataStore работать будет
        if (localDataSource.getFavouriteById(route.id) != null) {
            localDataSource.removeFavourite(route.id)
        } else {
            localDataSource.addFavourite(route.copy(isFavourite = true))
        }
    }
}

//TODO сделать нормальный DI
object RepositoryProvider {

    // локальные и remote источники (можно подменять на моки или реальные)
    private val localDataSource = LocalDataSourceMock()
    private val remoteDataSource = RemoteDataSourceMock()

    // сам репозиторий, создаётся один раз
    val routeRepository: RouteRepository by lazy {
        RouteRepositoryImpl(localDataSource, remoteDataSource)
    }
}