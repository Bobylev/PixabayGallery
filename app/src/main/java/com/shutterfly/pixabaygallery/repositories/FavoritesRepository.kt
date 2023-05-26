package com.shutterfly.pixabaygallery.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class FavoritesRepository(private val context: Context) {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore("favorites")
        val FAVORITES_NAME_KEY = stringPreferencesKey("FAVORITES_LIST")
    }

    suspend fun saveFavorites(favorites: Collection<Int>) {
        context.dataStore.edit { preferences ->
            preferences[FAVORITES_NAME_KEY] = Gson().toJson(favorites.toIntArray())
        }
    }

    suspend fun getFavorites(): Collection<Int> {
        val result = context.dataStore.data.map { preferences ->
            preferences[FAVORITES_NAME_KEY] ?: ""
        }.first()
        return (Gson().fromJson(result, IntArray::class.java)?: IntArray(0){0}).toList()
    }
}