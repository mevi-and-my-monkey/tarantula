package com.mevi.tarantula.core

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("user_prefs")

class PreferencesHelper(private val context: Context) {

    // Claves para los datos
    companion object {
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_IS_ADMIN = booleanPreferencesKey("user_is_admin")
        val USER_PHONE_NUMBER = stringPreferencesKey("user_is_phone_number")
    }

    suspend fun saveUserData(email: String, name: String, isAdmin: Boolean, phoneNumber: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
            preferences[USER_NAME] = name
            preferences[USER_IS_ADMIN] = isAdmin
            preferences[USER_PHONE_NUMBER] = phoneNumber
        }
    }

    val userEmail: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_EMAIL]
        }

    val userName: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_NAME]
        }

    val userIsAdmin: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[USER_IS_ADMIN] ?: false
        }

    val userPhoneNumber: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_PHONE_NUMBER]
        }
}