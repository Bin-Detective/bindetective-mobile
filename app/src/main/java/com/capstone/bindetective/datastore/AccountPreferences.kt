package com.capstone.bindetective.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFERENCES_NAME = "account_preferences" // Declare this before usage

class AccountPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

        private val USER_NAME_KEY = stringPreferencesKey("user_name") // Example key for user name

        @Volatile
        private var INSTANCE: AccountPreferences? = null

        fun getInstance(context: Context): AccountPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = AccountPreferences(context.dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    // Save the user name or other data
    suspend fun saveUserName(userName: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = userName
        }
    }

    // Retrieve the user name
    fun getUserName(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_NAME_KEY]
        }
    }

    // Clear the user data (useful for sign-out or when you want to remove all stored data)
    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.remove(USER_NAME_KEY) // Optionally, remove other keys
        }
    }
}
