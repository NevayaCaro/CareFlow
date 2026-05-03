package com.nevaya.careflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nevaya.careflow.data.SessionStore
import com.nevaya.careflow.ui.theme.GreenDark
import com.nevaya.careflow.ui.theme.GreenPrimary

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
            .background(Color(0xFFF4F6F8))
    ) {

        Surface(
            color = GreenPrimary,
            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
            shadowElevation = 6.dp
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
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Shift Board",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Code: ${session.code}",
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))


        if (selectedName == null) {

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(grouped.keys.toList()) { name ->

                    val assignment = grouped[name]?.firstOrNull()

                    val rooms = assignment?.rooms
                        ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
                        ?: emptyList()

                    val showers = assignment?.showers
                        ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
                        ?: emptyList()

                    val meals = assignment?.meals
                        ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
                        ?: emptyList()

                    val workload = when {
                        rooms.size <= 3 -> "Light"
                        rooms.size <= 7 -> "Moderate"
                        else -> "Heavy"
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { selectedName = name },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(18.dp),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {

                        Column(Modifier.padding(16.dp)) {

                            Text(
                                "$name (${assignment?.role ?: "CNA"})",
                                fontWeight = FontWeight.Bold,
                                color = GreenDark
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                "${rooms.size} rooms • ${showers.size} showers • ${meals.size} meals",
                                color = Color.Gray
                            )

                            Text(
                                "Workload: $workload",
                                color = when (workload) {
                                    "Light" -> Color(0xFF2E7D32)
                                    "Moderate" -> Color(0xFFF9A825)
                                    else -> Color(0xFFC62828)
                                },
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }


        else {

            val assignment = grouped[selectedName]?.firstOrNull()

            val rooms = assignment?.rooms
                ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
                ?: emptyList()

            val showers = assignment?.showers
                ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
                ?: emptyList()

            val meals = assignment?.meals
                ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
                ?: emptyList()

            Column(Modifier.padding(16.dp)) {

                Text(
                    selectedName ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    color = GreenDark,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    assignment?.role ?: "",
                    color = Color.Gray
                )

                Spacer(Modifier.height(16.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LegendItem(GreenPrimary, "Assigned")
                    LegendItem(Color(0xFF42A5F5), "Showers")
                    LegendItem(Color(0xFFFFA726), "Meals")
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    "Room Schedule",
                    color = GreenPrimary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))


                RoomGrid(
                    rooms = session.rooms ?: emptyList(),
                    assignedRooms = rooms,
                    showerRooms = showers,
                    mealRooms = meals
                )
            }
        }
    }
}



@Composable
fun RoomGrid(
    rooms: List<Int>,
    assignedRooms: List<String>,
    showerRooms: List<String>,
    mealRooms: List<String>
) {

    val assigned = assignedRooms.mapNotNull { it.toIntOrNull() }
    val showers = showerRooms.mapNotNull { it.toIntOrNull() }
    val meals = mealRooms.mapNotNull { it.toIntOrNull() }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        rooms.chunked(5).forEach { row ->

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                row.forEach { room ->

                    val isAssigned = assigned.contains(room)
                    val isShower = showers.contains(room)
                    val isMeal = meals.contains(room)

                    val bg = when {
                        isShower -> Color(0xFF42A5F5)
                        isMeal -> Color(0xFFFFA726)
                        isAssigned -> GreenPrimary
                        else -> Color.White
                    }

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(bg)
                            .border(1.dp, Color.LightGray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            room.toString(),
                            color = if (bg == Color.White) Color.Gray else Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )

        Spacer(Modifier.width(6.dp))

        Text(
            label,
            color = Color.DarkGray,
            style = MaterialTheme.typography.bodySmall
        )
    }
}