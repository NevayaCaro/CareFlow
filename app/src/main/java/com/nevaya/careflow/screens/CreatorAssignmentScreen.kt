package com.nevaya.careflow.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nevaya.careflow.data.SessionStore
import com.nevaya.careflow.ui.theme.GreenDark
import com.nevaya.careflow.ui.theme.GreenPrimary
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun CreatorAssignmentScreen(
    sessionCode: String,
    onBack: () -> Unit
) {

    val session = SessionStore.getSession(sessionCode)

    if (session == null) {
        Text("Invalid session code")
        return
    }

    var selectedName by remember { mutableStateOf<String?>(null) }
    val grouped = session.assignments.groupBy { it.name }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
    ) {

        //  TOP BAR
        Surface(
            color = GreenPrimary,
            shadowElevation = 6.dp,
            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                IconButton(
                    onClick = {
                        if (selectedName != null) selectedName = null
                        else onBack()
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                }

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Shift Overview",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        "Code: ${session.code}",
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        if (selectedName == null) {

            // STAFF LIST (manager view)
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(grouped.keys.toList()) { name ->

                    val assignment = grouped[name]?.firstOrNull()

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { selectedName = name },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {

                        Column(Modifier.padding(16.dp)) {

                            Text(
                                name,
                                style = MaterialTheme.typography.titleMedium,
                                color = GreenDark
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                assignment?.role ?: "",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                "Tap to view full assignment",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

        } else {

            val assignment = grouped[selectedName]?.firstOrNull()

            val role = assignment?.role ?: "Unknown"

            val rooms = assignment?.rooms
                ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
                ?: emptyList()

            val showers = assignment?.showers
                ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
                ?: emptyList()

            val meals = assignment?.meals
                ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
                ?: emptyList()

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                //  HEADER
                Text(
                    selectedName ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    color = GreenDark
                )

                Text(
                    role,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(16.dp))

                // ROOMS
                InfoCard(
                    title = "Rooms",
                    content = rooms.joinToString(", ")
                )

                Spacer(Modifier.height(12.dp))

                //  SHOWERS
                InfoListCard(
                    title = "Showers",
                    items = showers.map { "Room $it" }
                )

                Spacer(Modifier.height(12.dp))

                // MEALS
                InfoListCard(
                    title = "Meals",
                    items = meals.map { "Room $it" }
                )
            }
        }
    }
}


@Composable
fun InfoCard(title: String, content: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, color = GreenPrimary)
            Spacer(Modifier.height(6.dp))
            Text(content, color = Color.DarkGray)
        }
    }
}

@Composable
fun InfoListCard(title: String, items: List<String>) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, color = GreenPrimary)
            Spacer(Modifier.height(6.dp))
            items.forEach {
                Text(it, color = Color.DarkGray)
            }
        }
    }
}