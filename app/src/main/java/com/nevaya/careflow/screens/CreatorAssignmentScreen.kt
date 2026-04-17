package com.nevaya.careflow.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nevaya.careflow.data.SessionStore

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
            .padding(16.dp)
    ) {

        //  SMALL TOP LEFT BACK BUTTON
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Button(
                onClick = { onBack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .height(36.dp)
                    .wrapContentWidth(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
            ) {
                Text("Back")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("CREATOR SHIFT VIEW")
        Text("Code: ${session.code}")

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedName == null) {

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                items(grouped.keys.toList()) { name ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
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
            val first = assignments.firstOrNull()

            val role = first?.role ?: "Unknown"

            val rooms = first?.rooms
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?: emptyList()

            val showers = first?.showers
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?: emptyList()

            val meals = first?.meals
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?: emptyList()

            Button(onClick = { selectedName = null }) {
                Text("Back")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("$selectedName ($role)")

            Spacer(modifier = Modifier.height(12.dp))

            Text("Rooms: ${rooms.joinToString(", ")}")

            Spacer(modifier = Modifier.height(12.dp))

            Text("Showers")
            showers.forEach { room ->
                Text("Room $room")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Meals")
            meals.forEach { room ->
                Text("Room $room")
            }
        }
    }
}