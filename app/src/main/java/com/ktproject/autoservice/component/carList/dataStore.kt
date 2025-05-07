package com.ktproject.autoservice.component.carList

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "car_preferences")

class SelectedMakeStore(private val context: Context) {
    companion object {
        private val SELECTED_MAKE = stringPreferencesKey("selected_make")
    }

    suspend fun saveMake(make: String) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_MAKE] = make
        }
    }

    suspend fun loadMake(): String? {
        return context.dataStore.data.map { it[SELECTED_MAKE] }.first()
    }
}
