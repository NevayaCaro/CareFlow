package com.nevaya.careflow.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nevaya.careflow.data.SessionStore
import com.nevaya.careflow.data.NurseAssignment

@Composable
fun WorkerAssignmentScreen(sessionCode: String) {

    val session = SessionStore.getSession(sessionCode)

    if (session == null) {
        Text("Invalid session code")
        return
    }

    var selectedName by remember { mutableStateOf<String?>(null) }

    // 🔥 GROUP SAFE (explicit type fixes inference issue)
    val grouped: Map<String, List<NurseAssignment>> =
        session.assignments.groupBy { it.name }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "SHIFT BOARD",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Code: ${session.code}")

        Spacer(modifier = Modifier.height(20.dp))

        if (selectedName == null) {


            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(grouped.keys.toList()) { name ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                indication = null
                            ) {
                                selectedName = name
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = name)
                        }
                    }
                }
            }

        } else {


            val assignments = grouped[selectedName].orEmpty()
            // ensures missing role doesn't break UI display
            val role = assignments.firstOrNull()?.role?.takeIf { it.isNotBlank() } ?: "Unknown"

            // keeps room values as strings so app never crashes on formatting issues
            val rooms = assignments.flatMap {
                it.rooms.split(",")
                    .map { r -> r.trim() }
                    .filter { it.isNotEmpty() }
            }
            // prevents crashes from bad formatting or empty strings
            val showers = assignments.flatMap {
                it.showers.split(",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
            }

            val meals = assignments.flatMap {
                it.meals.split(",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
            }

            Column {

                Button(onClick = { selectedName = null }) {
                    Text("Back")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "$selectedName ($role)",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Rooms - " +
                            if (rooms.isEmpty()) "None"
                            else rooms.sorted().joinToString(", ")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Showers - " +
                            if (showers.isEmpty()) "None"
                            else showers.sorted().joinToString(", ")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Meals - " +
                            if (meals.isEmpty()) "None"
                            else meals.sorted().joinToString(", ")
                )
            }
        }
    }
}