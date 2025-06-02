package com.example.cafesolace

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create a DataStore instance named "settings" associated with the Context
private val Context.dataStore by preferencesDataStore(name = "settings")

object ThemePreference {
    // Define a key to store the boolean value representing dark theme preference
    private val THEME_KEY = booleanPreferencesKey("is_dark_theme")

    // Suspend function to save the user's theme preference (dark or light) into DataStore
    suspend fun setTheme(context: Context, isDark: Boolean) {
        context.dataStore.edit { settings ->
            settings[THEME_KEY] = isDark
        }
    }

    // Function to retrieve the saved theme preference as a Flow<Boolean?>
    // Emits the current stored value or null if not set yet
    fun getTheme(context: Context): Flow<Boolean?> {
        return context.dataStore.data.map { preferences ->
            preferences[THEME_KEY]
        }
    }

    // Suspend function to clear the stored theme preference from DataStore
    suspend fun clearTheme(context: Context) {
        context.dataStore.edit { it.remove(THEME_KEY) }
    }
}
