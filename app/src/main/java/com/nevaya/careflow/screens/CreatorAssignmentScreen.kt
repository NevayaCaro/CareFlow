package com.nevaya.careflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nevaya.careflow.data.SessionStore
import com.nevaya.careflow.ui.theme.GreenDark
import com.nevaya.careflow.ui.theme.GreenPrimary
import androidx.compose.foundation.lazy.items

@Composable
fun CreatorAssignmentScreen(
    sessionCode: String,
    onBack: () -> Unit
) {

    val session = SessionStore.getSessionByCreatorCode(sessionCode)
        ?: SessionStore.getSession(sessionCode)
        ?: run {
            Text("Invalid session code")
            return
        }

    var selectedName by remember { mutableStateOf<String?>(null) }

    val assignments = session.assignments
    val grouped = assignments.groupBy { it.name }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
    ) {

        // ================= TOP BAR =================
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
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Shift Overview", color = Color.White)
                    Text(
                        "Code: ${session.code}",
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ================= LIST =================
        if (selectedName == null) {

            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(grouped.keys.toList()) { name ->

                    val assignment = grouped[name]?.firstOrNull()

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedName = name },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) {

                        Column(Modifier.padding(16.dp)) {

                            Text(
                                text = name,
                                style = MaterialTheme.typography.titleMedium,
                                color = GreenDark
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = assignment?.role ?: "No role",
                                color = Color.Gray
                            )

                            Spacer(Modifier.height(6.dp))

                            Text("Tap to view assignment", color = Color.Gray)
                        }
                    }
                }
            }

        } else {

            val assignment = grouped[selectedName]?.firstOrNull() ?: return@Column

            val rooms = assignment.rooms
                .split(",")
                .mapNotNull { it.trim().toIntOrNull() }

            val showers = assignment.showers
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            val meals = assignment.meals
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            Column(Modifier.padding(16.dp)) {

                Text(selectedName ?: "", style = MaterialTheme.typography.headlineSmall)
                Text(assignment.role, color = Color.Gray)

                Spacer(Modifier.height(16.dp))

                InfoCard("Rooms", rooms.joinToString(", "))

                Spacer(Modifier.height(12.dp))

                InfoListCard("Showers", showers.map { "Room $it" })

                Spacer(Modifier.height(12.dp))

                InfoListCard("Meals", meals.map { "Room $it" })
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