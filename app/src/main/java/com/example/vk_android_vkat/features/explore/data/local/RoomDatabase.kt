package com.example.vk_android_vkat.features.explore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Route::class],
    version = 2,                           // <-- увеличить
    exportSchema = false
)
@TypeConverters(Converters::class)        // <-- добавить
abstract class AppDatabase : RoomDatabase() {
    abstract fun routeDao(): RouteDao
}