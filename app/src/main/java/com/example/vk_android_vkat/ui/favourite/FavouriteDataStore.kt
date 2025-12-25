package com.example.vk_android_vkat.ui.favourite

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("favourites")

class FavouriteDataStore(private val context: Context) {

    private val FAVOURITES_KEY = stringSetPreferencesKey("favourite_routes")

    val favouritesFlow: Flow<Set<Long>> =
        context.dataStore.data.map { prefs ->
            prefs[FAVOURITES_KEY]
                ?.map { it.toLong() }
                ?.toSet()
                ?: emptySet()
        }

    suspend fun toggleFavourite(routeId: Long) {
        context.dataStore.edit { prefs ->
            val current = prefs[FAVOURITES_KEY]?.toMutableSet() ?: mutableSetOf()
            val id = routeId.toString()

            if (current.contains(id)) {
                current.remove(id)
            } else {
                current.add(id)
            }

            prefs[FAVOURITES_KEY] = current
        }
    }
}