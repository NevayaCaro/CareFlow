package com.nevaya.careflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.nevaya.careflow.data.SessionStore
import com.nevaya.careflow.data.NurseAssignment
import com.nevaya.careflow.data.TaskItem
import com.nevaya.careflow.ui.theme.GreenPrimary
import com.nevaya.careflow.ui.theme.GreenDark

@Composable
fun WorkerAssignmentScreen(sessionCode: String) {

    val session = SessionStore.getSession(sessionCode)

    if (session == null) {
        Text("Invalid session code")
        return
    }

    var selectedName by remember { mutableStateOf<String?>(null) }

    val grouped: Map<String, List<NurseAssignment>> =
        session.assignments.groupBy { it.name }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // TOP BAR
        Surface(
            color = GreenPrimary,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Shift Board",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // CODE + BACK BUTTON ROW
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = { selectedName = null },
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text("Back", color = Color.White)
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Code: ${session.code}",
                style = MaterialTheme.typography.bodyMedium,
                color = GreenDark
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // LIST OF WORKERS
        if (selectedName == null) {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {

                items(grouped.keys.toList()) { name ->

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                selectedName = name
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = name,
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

        } else {

            val assignments = grouped[selectedName].orEmpty()
            val selectedAssignment = assignments.firstOrNull()

            val role = assignments.firstOrNull()?.role?.takeIf { it.isNotBlank() } ?: "Unknown"

            val rooms = selectedAssignment?.rooms
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?: emptyList()

            val showerTasks = selectedAssignment?.showers
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?.map { TaskItem(it.toInt()) }
                ?: emptyList()

            val mealTasks = selectedAssignment?.meals
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?.map { TaskItem(it.toInt()) }
                ?: emptyList()

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "$selectedName ($role)",
                    style = MaterialTheme.typography.headlineSmall,
                    color = GreenDark
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ROOMS CARD
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Rooms", color = Color.White, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            if (rooms.isEmpty()) "None"
                            else rooms.sorted().joinToString(", "),
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // SHOWERS CARD
                Text("Showers", style = MaterialTheme.typography.titleMedium, color = GreenDark)
                Spacer(modifier = Modifier.height(8.dp))

                showerTasks.forEach { task ->

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text("Room ${task.room}", color = Color.White)
                                Text(
                                    if (task.done)
                                        "Done at ${task.completedAt}"
                                    else
                                        "Not completed",
                                    color = Color.White
                                )
                            }

                            Button(
                                onClick = {
                                    task.done = true
                                    task.completedAt = java.text.SimpleDateFormat("HH:mm MM/dd")
                                        .format(java.util.Date())
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                            ) {
                                Text("Done", color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // MEALS CARD
                Text("Meals", style = MaterialTheme.typography.titleMedium, color = GreenDark)
                Spacer(modifier = Modifier.height(8.dp))

                mealTasks.forEach { task ->

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text("Room ${task.room}", color = Color.White)
                                Text(
                                    if (task.done)
                                        "Done at ${task.completedAt}"
                                    else
                                        "Not completed",
                                    color = Color.White
                                )
                            }

                            Button(
                                onClick = {
                                    task.done = true
                                    task.completedAt = java.text.SimpleDateFormat("HH:mm MM/dd")
                                        .format(java.util.Date())
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                            ) {
                                Text("Done", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
