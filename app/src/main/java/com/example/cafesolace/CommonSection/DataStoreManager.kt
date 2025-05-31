package com.example.cafesolace.Data

import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create a DataStore instance associated with the Context using the name "profile_data"
private val Context.dataStore by preferencesDataStore(name = "profile_data")

// Class to manage user profile data using Jetpack DataStore (Preferences)
class ProfileDataStore(private val context: Context) {

    companion object {
        // Keys used to store/retrieve preferences values
        val USERNAME = stringPreferencesKey("username")
        val EMAIL = stringPreferencesKey("email")
        val PASSWORD = stringPreferencesKey("password")
        val CONTACT = stringPreferencesKey("contact")
        val IMAGE_URI = stringPreferencesKey("image_uri")
    }

    // Flows to observe data changes; return stored value or default empty string if none exists
    val getUsername: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USERNAME] ?: ""
    }
    val getEmail: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[EMAIL] ?: ""
    }
    val getPassword: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PASSWORD] ?: ""
    }
    val getContact: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CONTACT] ?: ""
    }
    val getImageUri: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[IMAGE_URI] ?: ""
    }

    // Suspend function to save user profile data into DataStore
    suspend fun saveProfile(
        username: String,
        email: String,
        password: String,
        contact: String,
        imageUri: Uri?
    ) {
        // Edit preferences atomically
        context.dataStore.edit { preferences ->
            preferences[USERNAME] = username
            preferences[EMAIL] = email
            preferences[PASSWORD] = password
            preferences[CONTACT] = contact
            // Store image URI as String, or empty string if null
            preferences[IMAGE_URI] = imageUri?.toString() ?: ""
        }
    }
}
