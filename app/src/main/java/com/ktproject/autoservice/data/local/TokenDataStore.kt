package com.ktproject.autoservice.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class TokenDataStore(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token_key")
    }

    // Поток для наблюдения за токеном
    val token: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY] // Извлекаем значение по ключу TOKEN_KEY
    }

    // Сохранение токена
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token // Сохраняем токен в DataStore
        }
    }

    // Очистка токена
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY) // Удаляем токен
        }
    }

    // Синхронный метод для получения токена (не через Flow)
    suspend fun getToken(): String? {
        return context.dataStore.data
            .map { preferences -> preferences[TOKEN_KEY] } // Извлекаем токен
            .firstOrNull()  // Берем первое значение (или null, если его нет)
    }
}
