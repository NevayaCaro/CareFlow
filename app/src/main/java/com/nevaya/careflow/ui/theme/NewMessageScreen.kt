package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nevaya.careflow.data.ConversationRepository

data class AppUser(
    val id: String,
    val name: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMessageScreen(
    navController: NavHostController,
    onBack: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Start New Message") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        var searchQuery by remember { mutableStateOf("") }

        // Temporary static user list (can be replaced with real data later)
        val users = listOf(
            AppUser("u1", "Nurse Admin"),
            AppUser("u2", "Supervisor"),
            AppUser("u3", "Dr. Smith"),
            AppUser("u4", "Nurse Kelly"),
            AppUser("u5", "Nurse Jordan"),
            AppUser("u6", "Dr. Patel")
        )

        val filteredUsers = users.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search users...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredUsers) { user ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    // Create or get existing conversation
                                    val convo = ConversationRepository.getOrCreateConversation(
                                        title = user.name,
                                        participants = listOf("Me", user.name)
                                    )

                                    // Navigate to chat screen
                                    navController.navigate("chat/${convo.id}")
                                }
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            user.name,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

