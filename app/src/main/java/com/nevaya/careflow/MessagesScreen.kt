package com.nevaya.careflow.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.nevaya.careflow.data.ConversationRepository
import com.nevaya.careflow.data.ConversationData
import com.nevaya.careflow.ui.components.MainScreenWithFloatingMenu
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.ripple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    navController: NavHostController,
    padding: PaddingValues
) {

    MainScreenWithFloatingMenu(navController) { innerPadding ->

        var searchQuery by remember { mutableStateOf("") }

        // ⭐ REAL conversations from repository
        val conversations by ConversationRepository.conversations.collectAsState()

        // Convert map to list
        val convoList: List<ConversationData> = conversations.values.toList()

        // ⭐ Sort pinned first, then by last message time
        val sortedConversations = convoList.sortedWith(
            compareByDescending<ConversationData> { it.profilePictures.containsKey("pinned") }
                .thenByDescending { it.messages.lastOrNull()?.timestamp ?: 0L }
        )

        // ⭐ Filter by search
        val filteredConversations = sortedConversations.filter { convo ->
            val lastMsg = convo.messages.lastOrNull()?.text ?: ""
            convo.title.contains(searchQuery, ignoreCase = true) ||
                    lastMsg.contains(searchQuery, ignoreCase = true)
        }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("new_message") },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "New Message")
                }
            }
        ) { fabPadding ->

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(fabPadding)
                    .padding(10.dp)
                    .fillMaxSize()
            ) {

                Text(
                    "Messages",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search conversations...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))


                if (filteredConversations.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No conversations yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredConversations) { convo ->

                            val lastMessage = convo.messages.lastOrNull()?.text ?: "No messages yet"
                            val lastTime = convo.messages.lastOrNull()?.timestamp?.let {
                                SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(it))
                            } ?: ""

                            val unreadCount = convo.messages.count { !it.seen && it.sender != "me" }

                            ConversationRow(
                                convo = convo,
                                lastMessage = lastMessage,
                                time = lastTime,
                                unreadCount = unreadCount,
                                onClick = {
                                    navController.navigate("chat/${convo.id}")
                                }
                            )
                        }
                    }
                }


                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredConversations) { convo ->

                        val lastMessage = convo.messages.lastOrNull()?.text ?: "No messages yet"
                        val lastTime = convo.messages.lastOrNull()?.timestamp?.let {
                            SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(it))
                        } ?: ""

                        val unreadCount = convo.messages.count { !it.seen && it.sender != "me" }

                        ConversationRow(
                            convo = convo,
                            lastMessage = lastMessage,
                            time = lastTime,
                            unreadCount = unreadCount,
                            onClick = {
                                navController.navigate("chat/${convo.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationRow(
    convo: ConversationData,
    lastMessage: String,
    time: String,
    unreadCount: Int,
    onClick: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    var showMenu by remember { mutableStateOf(false) }

    val animatedOffset by animateFloatAsState(offsetX)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = animatedOffset.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    offsetX += dragAmount

                    if (offsetX < -120) {
                        ConversationRepository.deleteConversation(convo.id)
                        offsetX = 0f
                    }
                    if (offsetX > 120) {
                        ConversationRepository.pinConversation(convo.id)
                        offsetX = 0f
                    }
                }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick
            )

            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ⭐ Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            val avatarUrl = convo.profilePictures[convo.title] ?: ""

            if (avatarUrl.isNotEmpty()) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize()
                )
            } else if (convo.participants.size > 2) {
                Icon(
                    Icons.Default.Group,
                    contentDescription = "Group",
                    tint = MaterialTheme.colorScheme.secondary
                )
            } else {
                Text(
                    convo.title.first().toString(),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                convo.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                lastMessage.replace(":)", "😊").replace(":(", "☹️"),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Column(horizontalAlignment = Alignment.End) {

            Text(
                time,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            if (unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        unreadCount.toString(),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            IconButton(onClick = { showMenu = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Pin") },
                    onClick = {
                        ConversationRepository.pinConversation(convo.id)
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Mute") },
                    onClick = {
                        ConversationRepository.muteConversation(convo.id)
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Archive") },
                    onClick = {
                        ConversationRepository.archiveConversation(convo.id)
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        ConversationRepository.deleteConversation(convo.id)
                        showMenu = false
                    }
                )
            }
        }
    }
}
