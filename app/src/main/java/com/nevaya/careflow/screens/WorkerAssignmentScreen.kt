package com.nevaya.careflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.nevaya.careflow.data.SessionStore
import com.nevaya.careflow.ui.theme.GreenPrimary
import com.nevaya.careflow.ui.theme.GreenDark

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

    var selectedName by remember { mutableStateOf<String?>(null) }

    val grouped = session.assignments.groupBy { it.name }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // TOP BAR
        Surface(
            color = GreenPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                IconButton(
                    onClick = { onBack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Shift Board",
                        color = Color.White
                    )

                    // shows generated / session code
                    Text(
                        text = "Code: ${session.code}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // WORKER LIST
        if (selectedName == null) {

            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(grouped.keys.toList()) { name ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                selectedName = name
                            },
                        colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = name,
                            color = Color.White,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
            }

        } else {

            TextButton(onClick = { selectedName = null }) {
                Text("← Back", color = GreenDark)
            }

            val assignment = grouped[selectedName]?.firstOrNull()

            val showerRooms = assignment?.showers
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?: emptyList()

            val mealRooms = assignment?.meals
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?: emptyList()

            val showerState = remember(selectedName) {
                mutableStateMapOf<String, Boolean>().apply {
                    showerRooms.forEach { put(it, false) }
                }
            }

            val mealState = remember(selectedName) {
                mutableStateMapOf<String, Boolean>().apply {
                    mealRooms.forEach { put(it, false) }
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

                Text(
                    text = selectedName ?: "",
                    color = GreenDark,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(20.dp))

                // SHOWERS
                Text("Showers", color = GreenDark)

                showerRooms.forEach { room ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GreenPrimary)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text("Room $room", color = Color.White)

                                Text(
                                    if (showerState[room] == true) "COMPLETED"
                                    else "Not completed",
                                    color = Color.White
                                )
                            }

                            Button(
                                onClick = { showerState[room] = true },
                                colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                            ) {
                                Text("Done", color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // MEALS
                Text("Meals", color = GreenDark)

                mealRooms.forEach { room ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GreenPrimary)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text("Room $room", color = Color.White)

                                Text(
                                    if (mealState[room] == true) "COMPLETED"
                                    else "Not completed",
                                    color = Color.White
                                )
                            }

                            Button(
                                onClick = { mealState[room] = true },
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