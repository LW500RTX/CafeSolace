package com.example.cafesolace.Data

import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "profile_data")

class ProfileDataStore(private val context: Context) {

    companion object {
        val USERNAME = stringPreferencesKey("username")
        val EMAIL = stringPreferencesKey("email")
        val PASSWORD = stringPreferencesKey("password")
        val CONTACT = stringPreferencesKey("contact")
        val IMAGE_URI = stringPreferencesKey("image_uri")
    }

    val getUsername: Flow<String> = context.dataStore.data.map { it[USERNAME] ?: "" }
    val getEmail: Flow<String> = context.dataStore.data.map { it[EMAIL] ?: "" }
    val getPassword: Flow<String> = context.dataStore.data.map { it[PASSWORD] ?: "" }
    val getContact: Flow<String> = context.dataStore.data.map { it[CONTACT] ?: "" }
    val getImageUri: Flow<String> = context.dataStore.data.map { it[IMAGE_URI] ?: "" }

    suspend fun saveProfile(
        username: String,
        email: String,
        password: String,
        contact: String,
        imageUri: Uri?
    ) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME] = username
            preferences[EMAIL] = email
            preferences[PASSWORD] = password
            preferences[CONTACT] = contact
            preferences[IMAGE_URI] = imageUri?.toString() ?: ""
        }
    }
}
