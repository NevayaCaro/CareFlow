package com.nevaya.careflow.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.nevaya.careflow.data.model.Patient
import com.nevaya.careflow.data.models.RoomState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

val Context.roomDataStore by preferencesDataStore("room_data_store")

class RoomDataStore(private val context: Context) {

    private val ROOMS_KEY = stringPreferencesKey("rooms_json")

    fun getRooms(): Flow<List<RoomState>> =
        context.roomDataStore.data.map { prefs ->
            val json = prefs[ROOMS_KEY] ?: "[]"
            Json.decodeFromString(json)
        }

    suspend fun saveRooms(rooms: List<RoomState>) {
        context.roomDataStore.edit { prefs ->
            prefs[ROOMS_KEY] = Json.encodeToString(rooms)
        }
    }
}
