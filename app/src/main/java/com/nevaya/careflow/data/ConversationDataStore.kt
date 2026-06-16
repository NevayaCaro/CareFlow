package com.nevaya.careflow.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

val Context.conversationStore by preferencesDataStore("conversations")

object ConversationDataStore {

    private val KEY_CONVERSATIONS = stringPreferencesKey("conversations_json")

    suspend fun save(context: Context, conversations: Map<String, ConversationData>) {
        val json = Json.encodeToString(conversations)
        context.conversationStore.edit {
            it[KEY_CONVERSATIONS] = json
        }
    }

    suspend fun load(context: Context): Map<String, ConversationData> {
        val json = context.conversationStore.data
            .map { it[KEY_CONVERSATIONS] ?: "{}" }
            .first()

        return Json.decodeFromString(json)
    }
}
