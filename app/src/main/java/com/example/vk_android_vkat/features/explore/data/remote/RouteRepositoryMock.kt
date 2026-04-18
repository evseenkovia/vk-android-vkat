package com.example.vk_android_vkat.features.explore.data

import android.util.Log
import com.example.vk_android_vkat.data.mockRoutes
import com.example.vk_android_vkat.features.explore.data.local.AppDatabase
import com.example.vk_android_vkat.features.explore.data.local.toRoute
import com.example.vk_android_vkat.features.explore.data.local.toRouteModel
import com.example.vk_android_vkat.features.explore.domain.RouteModel
import com.example.vk_android_vkat.features.explore.domain.RouteRepository
import com.example.vk_android_vkat.features.explore.domain.filter.RouteFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RouteRepositoryMock(
    private val database: AppDatabase
) : RouteRepository {

    private val _routes = MutableStateFlow<List<RouteModel>>(emptyList())
    override val routesFlow: StateFlow<List<RouteModel>> = _routes.asStateFlow()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            val savedEntities = database.routeDao().getAll()
            val savedRoutes = savedEntities.map { it.toRouteModel() }
            val routesWithFavourites = checkFavourites(savedRoutes)   // ← ключевая строка
            Log.d("RouteRepo", "Loaded ${routesWithFavourites.size} routes from DB")
            _routes.value = if (routesWithFavourites.isNotEmpty()) {
                routesWithFavourites.toMutableList()
            } else {
                mockRoutes.toMutableList()
            }
        }
    }

    override suspend fun addRoute(route: RouteModel) {
        Log.d("RouteRepo", "Saving route: id=${route.id}, title=${route.title}, points=${route.points.size}, tags=${route.tags}")
        val routeEntity = route.toRoute()
        Log.d("RouteRepo", "Mapped to Route entity: id=${routeEntity.id}, title=${routeEntity.title}, points=${routeEntity.points.size}")
        try {
            database.routeDao().insertAll(routeEntity)
            Log.d("RouteRepo", "Insert successful")
            val allAfter = database.routeDao().getAll()
            Log.d("RouteRepo", "Total routes in DB after insert: ${allAfter.size}")
        } catch (e: Exception) {
            Log.e("RouteRepo", "Insert failed", e)
        }
        _routes.update { (it + route).toMutableList() }
    }


    override fun getNextId(): Int {
        return (_routes.value.maxOfOrNull { it.id } ?: 0) + 1
    }

    override suspend fun getAllRoutes(): Result<List<RouteModel>> {
        val currentRoutes = _routes.value
        return if (currentRoutes.isNotEmpty()) {
            val favouriteRoutes = checkFavourites(currentRoutes)
            Result.success(favouriteRoutes)
        } else {
            Result.failure(Exception("Ошибка загрузки маршрутов"))
        }
    }

    override suspend fun getRouteById(id: Int): Result<RouteModel> {
        return _routes.value.find { it.id == id }
            ?.let {
                checkFavourites(it)
                Result.success(it)
            }
            ?: Result.failure(Exception("Маршрут с id = $id не найден"))
    }

    override suspend fun getRouteByFilter(filter: RouteFilter): Result<List<RouteModel>> {
        val filteredRoutes = _routes.value.filter {
            it.rating >= filter.rating.start && it.rating <= filter.rating.endInclusive &&
                    it.durationHours >= filter.duration.start && it.durationHours <= filter.duration.endInclusive &&
                    it.distanceKm >= filter.distance.start && it.distanceKm <= filter.distance.endInclusive
        }
        return if (filteredRoutes.isNotEmpty()) {
            checkFavourites(filteredRoutes)
            Result.success(filteredRoutes)
        } else {
            Result.failure(Exception("По заданным фильтрам результаты не найдены"))
        }
    }

    override suspend fun findRouteByQuery(query: String?): Result<List<RouteModel>> {
        return if (query.isNullOrBlank()) {
            getAllRoutes()
        } else {
            val filteredRoutes = _routes.value.filter {
                it.title.lowercase().contains(query.lowercase())
            }
            if (filteredRoutes.isNotEmpty()) {
                checkFavourites(filteredRoutes)
                Result.success(filteredRoutes)
            } else {
                Result.failure(Exception("Результаты не найдены"))
            }
        }
    }

    override suspend fun addRouteToFavourites(vararg routeModels: RouteModel) {
        val mappedRoutes = routeModels.map { it.toRoute() }
        database.routeDao().insertAll(mappedRoutes)
    }

    override suspend fun getAllFavourites(): Result<List<RouteModel>> {
        val favourites = database.routeDao().getAll().map { it.toRouteModel() }.filter { it.isFavourite }
        return if (favourites.isNotEmpty()) {
            Result.success(favourites)
        } else {
            Result.failure(Exception("В Избранном пусто"))
        }
    }

    override suspend fun deleteFromFavourites(id: Int) {
        database.routeDao().deleteById(id)
    }

    suspend fun checkFavourites(route: RouteModel) {
        val localRoute = database.routeDao().getRouteById(route.id)
        route.isFavourite = localRoute?.isFavourite ?: false
    }

    fun updateRouteFavouriteStatus(routeId: Int, isFavourite: Boolean) {
        _routes.update { currentList ->
            currentList.map { route ->
                if (route.id == routeId) route.copy(isFavourite = isFavourite)
                else route
            }.toMutableList()
        }
    }

    suspend fun checkFavourites(routes: List<RouteModel>): List<RouteModel> {
        val ids = routes.map { it.id }.toIntArray()
        val favRoutes = database.routeDao().loadAllByIds(ids)
        val favIds = favRoutes.map { it.id }.toSet()
        return routes.map { route ->
            if (route.id in favIds) route.copy(isFavourite = true)
            else route.copy(isFavourite = false)
        }
    }
}