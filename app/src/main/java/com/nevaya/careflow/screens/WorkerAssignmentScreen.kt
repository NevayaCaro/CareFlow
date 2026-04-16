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
import com.nevaya.careflow.data.TaskItem

@Composable
fun WorkerAssignmentScreen(sessionCode: String) {
//gets session code
    val session = SessionStore.getSession(sessionCode)

//if session is missing
    if (session == null) {
        Text("Invalid session code")
        return
    }

    var selectedName by remember { mutableStateOf<String?>(null) }

    //  assignment by worker name
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

//shows list of workers
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

//selected worker data and first record
            val assignments = grouped[selectedName].orEmpty()
            val selectedAssignment = assignments.firstOrNull()


            // rooms assigned ONLY to this selected person
            val assignedRooms = assignments.flatMap {
                it.rooms.split(",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .mapNotNull { it.toIntOrNull() }
            }

            // ensures missing role doesn't break UI display
            val role = assignments.firstOrNull()?.role?.takeIf { it.isNotBlank() } ?: "Unknown"

            // clean room list for UI
            val rooms = selectedAssignment?.rooms
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?: emptyList()
            //converts shower to taskitem, this makes it able to be "completed"
            val showerTasks = selectedAssignment?.showers
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?.map { room -> TaskItem(room.toInt()) }
                ?: emptyList()
            //converts meals to a taskitem
            val mealTasks = selectedAssignment?.meals
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?.map { room -> TaskItem(room.toInt()) }
                ?: emptyList()
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

                Text("Showers", style = MaterialTheme.typography.titleMedium)

                showerTasks.forEach { task ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text("Room ${task.room}")
                                Text(
                                    if (task.done)
                                        "Done at ${task.completedAt}"
                                    else
                                        "Not completed"
                                )
                            }

                            Button(
                                onClick = {
                                    task.done = true
                                    task.completedAt = java.text.SimpleDateFormat("HH:mm MM/dd")
                                        .format(java.util.Date())
                                }
                            ) {
                                Text("Done")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Meals", style = MaterialTheme.typography.titleMedium)

                mealTasks.forEach { task ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text("Room ${task.room}")
                                Text(
                                    if (task.done)
                                        "Done at ${task.completedAt}"
                                    else
                                        "Not completed"
                                )
                            }

                            Button(
                                onClick = {
                                    task.done = true
                                    task.completedAt = java.text.SimpleDateFormat("HH:mm MM/dd")
                                        .format(java.util.Date())
                                }
                            ) {
                                Text("Done")
                            }
                        }
                    }
                }
            }
        }
    }
}