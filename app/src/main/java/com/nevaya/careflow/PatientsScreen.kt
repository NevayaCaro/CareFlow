package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun PatientsScreen(navController: NavHostController) {

    val fakePatients = listOf(
        Patient("Johnathan Reed", "High Fall Risk", "Assisted", "Regular Diet", "Needs help with mobility.", 7),
        Patient("Maria Lopez", "Moderate Fall Risk", "Independent", "Low Sodium", "Prefers warm blankets.", 5),
        Patient("Sarah Thompson", "Low Fall Risk", "Independent", "Diabetic Diet", "Monitor glucose levels.", 6),
        Patient("James Carter", "High Fall Risk", "Wheelchair", "Pureed Diet", "Requires full assistance.", 9)
    )

    val rooms = remember {
        (100..600 step 5).mapIndexed { index, roomNumber ->
            Room(
                roomNumber = roomNumber,
                patient = if (index < fakePatients.size) fakePatients[index] else null
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 70.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
    ) {

        Text(
            "Patient Rooms",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(rooms) { room ->
                Box(
                    modifier = Modifier
                        .height(120.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(12.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column {
                            Text("Room ${room.roomNumber}", style = MaterialTheme.typography.titleMedium)
                            Text(
                                room.patient?.name ?: "Empty",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }

                        Button(
                            onClick = {
                                navController.navigate("patient_card/${room.roomNumber}")
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("View")
                        }
                    }
                }
            }
        }
    }
}