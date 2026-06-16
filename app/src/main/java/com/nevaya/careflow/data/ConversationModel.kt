package com.nevaya.careflow.data

import kotlinx.serialization.Serializable


@Serializable
data class MessageData(
    val text: String,
    val sender: String,
    val timestamp: Long,
    val seen: Boolean = false
)

@Serializable
data class ConversationData(
    val id: String,
    val title: String,
    val participants: List<String>,
    val messages: MutableList<MessageData> = mutableListOf(),
    val profilePictures: Map<String, String> = emptyMap()
)

