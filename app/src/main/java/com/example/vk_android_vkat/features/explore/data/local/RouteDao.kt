package com.example.vk_android_vkat.features.explore.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RouteDao {

    @Query("SELECT * FROM route")
    suspend fun getAll(): List<Route>

    @Query("SELECT * FROM route WHERE route.id IN (:routeIds)")
    suspend fun loadAllByIds(routeIds: IntArray): List<Route>


    @Query("SELECT * FROM route WHERE route.id = :routeId")
    suspend fun getRouteById(routeId: Int): Route?


    @Query("SELECT * FROM route WHERE title LIKE :title")
    suspend fun findByTitle(title: String): List<Route>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg routes: Route)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(routes: List<Route>)

    @Delete
    suspend fun delete(route: Route)

    @Query("DELETE FROM route WHERE id = :routeId")
    suspend fun deleteById(routeId: Int)
}