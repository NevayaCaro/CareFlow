package com.nevaya.careflow.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userDataStore by preferencesDataStore("user_session")

class UserSessionDataStore(private val context: Context) {

    companion object {
        private val ACTIVE_USER = stringPreferencesKey("active_user")
        private val SAVED_USERS = stringPreferencesKey("saved_users")
    }

    val activeUser: Flow<String?> = context.userDataStore.data.map { prefs ->
        prefs[ACTIVE_USER]
    }

    val savedProfiles: Flow<String?> = context.userDataStore.data.map { prefs ->
        prefs[SAVED_USERS]
    }

    suspend fun setActiveUser(username: String) {
        context.userDataStore.edit { prefs ->
            prefs[ACTIVE_USER] = username
        }
    }

    suspend fun clearActiveUser() {
        context.userDataStore.edit { prefs ->
            prefs.remove(ACTIVE_USER)
        }
    }

    suspend fun saveProfiles(raw: String) {
        context.userDataStore.edit { prefs ->
            prefs[SAVED_USERS] = raw
        }
    }
}