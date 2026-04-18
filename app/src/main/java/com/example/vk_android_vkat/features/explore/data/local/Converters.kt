package com.example.vk_android_vkat.features.explore.data.local

import androidx.room.TypeConverter
import com.example.vk_android_vkat.features.editor.domain.RoutePointModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromStringList(value: List<String>): String = json.encodeToString(value)

    @TypeConverter
    fun toStringList(value: String): List<String> = json.decodeFromString(value)

    @TypeConverter
    fun fromPointsList(value: List<RoutePointModel>): String = json.encodeToString(value)

    @TypeConverter
    fun toPointsList(value: String): List<RoutePointModel> = json.decodeFromString(value)
}