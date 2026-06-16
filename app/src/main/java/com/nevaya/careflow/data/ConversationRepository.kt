package com.nevaya.careflow.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.nevaya.careflow.data.MessageData
import com.nevaya.careflow.data.ConversationData



object ConversationRepository {

    private val _conversations = MutableStateFlow<Map<String, ConversationData>>(emptyMap())
    val conversations: StateFlow<Map<String, ConversationData>> = _conversations

    private val _typingStatus = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val typingStatus: StateFlow<Map<String, Boolean>> = _typingStatus

    private val _onlineStatus = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val onlineStatus: StateFlow<Map<String, Boolean>> = _onlineStatus

    fun getOrCreateConversation(title: String, participants: List<String>): ConversationData {
        val id = "chat_" + participants.joinToString("_") { it.lowercase() }

        val existing = _conversations.value[id]
        if (existing != null) return existing

        val newConversation = ConversationData(
            id = id,
            title = title,
            participants = participants,
            messages = mutableListOf(),
            profilePictures = mapOf(
                title to "https://api.dicebear.com/7.x/initials/png?seed=${title.replace(" ", "")}"
            )
        )

        _conversations.value = _conversations.value + (id to newConversation)
        return newConversation
    }

    fun addMessage(conversationId: String, message: MessageData) {
        val convo = _conversations.value[conversationId] ?: return
        val updated = convo.copy(messages = (convo.messages + message).toMutableList())
        _conversations.value = _conversations.value + (conversationId to updated)
    }

    fun deleteConversation(conversationId: String) {
        _conversations.value = _conversations.value - conversationId
    }

    fun pinConversation(conversationId: String) {
        val convo = _conversations.value[conversationId] ?: return
        val updated = convo.copy(
            profilePictures = convo.profilePictures + ("pinned" to "true")
        )
        _conversations.value = _conversations.value + (conversationId to updated)
    }

    fun muteConversation(conversationId: String) {
        val convo = _conversations.value[conversationId] ?: return
        val updated = convo.copy(
            profilePictures = convo.profilePictures + ("muted" to "true")
        )
        _conversations.value = _conversations.value + (conversationId to updated)
    }

    fun archiveConversation(conversationId: String) {
        val convo = _conversations.value[conversationId] ?: return
        val updated = convo.copy(
            profilePictures = convo.profilePictures + ("archived" to "true")
        )
        _conversations.value = _conversations.value + (conversationId to updated)
    }

    fun setTyping(conversationId: String, isTyping: Boolean) {
        _typingStatus.value = _typingStatus.value + (conversationId to isTyping)
    }

    fun setOnline(user: String, online: Boolean) {
        _onlineStatus.value = _onlineStatus.value + (user to online)
    }

    fun getConversation(conversationId: String): ConversationData? {
        return _conversations.value[conversationId]
    }
}

