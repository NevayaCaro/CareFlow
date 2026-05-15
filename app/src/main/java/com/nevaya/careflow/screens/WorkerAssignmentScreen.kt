package com.nevaya.careflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.nevaya.careflow.data.NurseAssignment
import com.nevaya.careflow.ui.theme.GreenPrimary
import com.nevaya.careflow.ui.theme.GreenDark
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource

@Composable
fun WorkerAssignmentScreen(
    sessionCode: String,
    onBack: () -> Unit
) {

    val session = SessionStore.getSession(sessionCode)

    if (session == null) {
        Text("Invalid session code")
        return
    }

    var selectedWorker by remember { mutableStateOf<NurseAssignment?>(null) }
    var selectedRoom by remember { mutableStateOf<Int?>(null) }

    val assignments = session.assignments

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // TOP BAR
        Surface(color = GreenPrimary, modifier = Modifier.fillMaxWidth()) {
            Box(Modifier.padding(16.dp).fillMaxWidth()) {

                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Shift Board", color = Color.White)
                    Text("Code: ${session.code}", color = Color.White)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // STATE 1: WORKER LIST
        if (selectedWorker == null) {

            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(assignments) { worker ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                            ) {
                                selectedWorker = worker
                            },
                        colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(worker.name, color = Color.White)
                            Text(worker.role, color = Color.White)
                        }
                    }
                }
            }
        }

        // STATE 2: ROOM GRID
        else {

            TextButton(onClick = {
                selectedWorker = null
                selectedRoom = null
            }) {
                Text("← Back", color = GreenDark)
            }

            val worker = selectedWorker!!

            Text(
                text = "${worker.name} (${worker.role})",
                modifier = Modifier.padding(16.dp),
                color = GreenDark,
                style = MaterialTheme.typography.headlineSmall
            )

            // CONVERT STRING → LIST HERE ONLY
            val roomList = remember(worker.rooms) {
                worker.rooms
                    .split(",")
                    .mapNotNull { it.trim().toIntOrNull() }
            }

            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(roomList.chunked(3)) { row ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        row.forEach { room ->

                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(90.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                                    ) {
                                        selectedRoom = room
                                    },
                                colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(room.toString(), color = Color.White)
                                }
                            }
                        }

                        repeat(3 - row.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // STATE 3: ROOM DETAILS
        selectedRoom?.let { room ->

            val worker = selectedWorker!!
            val chart = session.roomCharts[room.toString()]

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {

                Column(Modifier.padding(16.dp)) {

                    Text("ROOM $room", style = MaterialTheme.typography.headlineSmall)

                    Spacer(Modifier.height(10.dp))

                    Text("Shower")
                    Text(
                        if (worker.showers.split(",").map { it.trim() }.contains(room.toString()))
                            "Assigned"
                        else "Not assigned"
                    )

                    Spacer(Modifier.height(10.dp))

                    Text("Linen")
                    Text(chart?.linen?.joinToString() ?: "None")

                    Spacer(Modifier.height(10.dp))

                    Text("Meals")
                    Text("Intake: ${chart?.mealIntake ?: "-"}")
                    Text("Output: ${chart?.mealOutputMl ?: "-"}")

                    Spacer(Modifier.height(10.dp))

                    Text("Tasks")
                    Text(chart?.tasks?.joinToString() ?: "None")

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = { selectedRoom = null },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                    ) {
                        Text("Close", color = Color.White)
                    }
                }
            }
        }
    }
}