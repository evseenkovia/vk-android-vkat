package com.example.vk_android_vkat.features.explore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Route::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routeDao() : RouteDao
}