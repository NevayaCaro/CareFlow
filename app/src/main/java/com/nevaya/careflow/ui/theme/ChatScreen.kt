package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nevaya.careflow.data.ConversationRepository
import com.nevaya.careflow.data.MessageData
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavHostController, conversationId: String) {

    val conversations by ConversationRepository.conversations.collectAsState()
    val typingStatus by ConversationRepository.typingStatus.collectAsState()
    val onlineStatus by ConversationRepository.onlineStatus.collectAsState()

    val conversation = conversations[conversationId]

    var inputText by remember { mutableStateOf("") }

    val messages = conversation?.messages ?: emptyList()

    val listState = rememberLazyListState()

    // Auto-scroll
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val isTyping = typingStatus[conversationId] ?: false
    val isOnline = onlineStatus[conversation?.title ?: ""] ?: false

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(conversation?.title ?: "Chat")
                        Text(
                            if (isOnline) "Online" else "Offline",
                            fontSize = 12.sp,
                            color = if (isOnline) Color(0xFF4CAF50) else Color.Gray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(
                        text = msg.text.replace(":)", "😊").replace(":(", "☹️"),
                        isMe = msg.sender == "me",
                        timestamp = msg.timestamp,
                        seen = msg.seen
                    )
                }

                if (isTyping) {
                    item {
                        TypingIndicator()
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = inputText,
                    onValueChange = {
                        inputText = it
                        ConversationRepository.setTyping(conversationId, it.isNotEmpty())
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message...") }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (inputText.isNotBlank()) {

                            val newMessage = MessageData(
                                text = inputText,
                                sender = "me",
                                timestamp = System.currentTimeMillis()
                            )

                            ConversationRepository.addMessage(conversationId, newMessage)
                            ConversationRepository.setTyping(conversationId, false)

                            inputText = ""
                        }
                    }
                ) {
                    Text("Send")
                }
            }
        }
    }
}

@Composable
fun ChatBubble(text: String, isMe: Boolean, timestamp: Long, seen: Boolean) {

    val time = SimpleDateFormat("h:mm a", Locale.getDefault()).format(timestamp)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {

        Column(horizontalAlignment = if (isMe) Alignment.End else Alignment.Start) {

            Box(
                modifier = Modifier
                    .background(
                        if (isMe) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(12.dp)
                    .widthIn(max = 260.dp)
            ) {
                Text(
                    text,
                    color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    time,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                if (isMe) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (seen) "Seen" else "Sent",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun TypingIndicator() {
    Text(
        "Typing...",
        color = MaterialTheme.colorScheme.secondary,
        fontSize = 14.sp,
        modifier = Modifier.padding(start = 8.dp)
    )
}
