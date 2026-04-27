package com.example.vk_android_vkat.features.explore.data.remote

import com.example.vk_android_vkat.data.mockRoutes
import com.example.vk_android_vkat.features.explore.data.local.AppDatabase
import com.example.vk_android_vkat.features.explore.data.local.toRoute
import com.example.vk_android_vkat.features.explore.data.local.toRouteModel
import com.example.vk_android_vkat.features.explore.domain.RouteModel
import com.example.vk_android_vkat.features.explore.domain.RouteRepository
import com.example.vk_android_vkat.features.explore.domain.filter.RouteFilter

class RouteRepositoryMock (// ссылка на экземпляр Room
    private val database: AppDatabase
) : RouteRepository {

    private val routes = mockRoutes // тестовые данные

    override suspend fun getAllRoutes(): Result<List<RouteModel>> {
        // Запрос к API
        val routes = mockRoutes
        return if (routes.isNotEmpty()){
            val favouriteRoutes = checkFavourites(routes)
            Result.success(favouriteRoutes)
        } else {
            Result.failure(Exception("Ошибка загрузки маршрутов"))
        }
    }

    override suspend fun getRouteById(id: Int): Result<RouteModel> {
        return routes.find { it.id == id }
            ?.let {
                // Проверяем, есть ли маршрут в избранном
                checkFavourites(it)
                Result.success(it) }
            ?: Result.failure(Exception("Маршрут с id = $id не найден" ))
    }

    override suspend fun getRouteByFilter(filter: RouteFilter): Result<List<RouteModel>> {
        val favRoutes = database.routeDao().getAll()
        val filteredRoutes = routes.filter {
            it.rating >= filter.rating.start && it.rating <= filter.rating.endInclusive &&
            it.durationHours >= filter.duration.start && it.durationHours <= filter.duration.endInclusive &&
            it.distanceKm >= filter.distance.start && it.distanceKm <= filter.distance.endInclusive
            //todo -> дописать еще фильтры
        }
        return if (filteredRoutes.isNotEmpty()){
            // Проверяем, есть ли маршруты в избранном
            checkFavourites(filteredRoutes)
            Result.success(filteredRoutes)
        }
        else
            Result.failure(Exception("По заданным фильтрам результаты не найдены"))
    }

    override suspend fun findRouteByQuery(query: String?): Result<List<RouteModel>> {
        return if (query.isNullOrBlank()) {
            getAllRoutes()
        } else {
            // Непустой запрос - фильтруем
            val filteredRoutes = mockRoutes.filter {
                it.title.lowercase().contains(query.lowercase())
            }

            if (filteredRoutes.isNotEmpty()) {
                val filteredFavouriteRoutes = checkFavourites(filteredRoutes)
                Result.success(filteredFavouriteRoutes)
            } else {
                Result.failure(Exception("Результаты не найдены"))
            }
        }
    }

    // Сохраняем маршруты из категории Избранное локально
    override suspend fun addRouteToFavourites(vararg routeModels: RouteModel) {
        val mappedRoutes = routeModels.map { it.toRoute() }
        database.routeDao().insertAll(mappedRoutes)
    }

    override suspend fun getAllFavourites(): Result<List<RouteModel>> {
        val favourites =
            database.routeDao().getAll().map { it.toRouteModel() }.filter { it.isFavourite }
        return if (favourites.isNotEmpty()) {
            Result.success(favourites)
        } else {
            Result.failure(Exception("В Избранном пусто "))
        }
    }

    // Удаляем маршрут из Избранного локально
    override suspend fun deleteFromFavourites(id: Int) {
        database.routeDao().deleteById(id)
    }

    suspend fun checkFavourites(route: RouteModel){
        val localRoute = database.routeDao().getRouteById(route.id)
        route.isFavourite = localRoute?.isFavourite ?: false
    }

    suspend fun checkFavourites(routes : List<RouteModel>) : List<RouteModel> {
        val ids = routes.map { it.id }.toIntArray()
        val favRoutes = database.routeDao().loadAllByIds(ids)
        val favIds = favRoutes.map { it.id }.toSet()
        return routes.map { route ->
            if (route.id in favIds) {
                route.copy(isFavourite = true)
            } else {
                route.copy(isFavourite = false)
            }
        }
    }
}