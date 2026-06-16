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
import com.nevaya.careflow.data.repository.RoomRepository
import com.nevaya.careflow.ui.components.MainScreenWithFloatingMenu

@Composable
fun PatientsScreen(navController: NavHostController) {

    MainScreenWithFloatingMenu(navController) { innerPadding ->

        val rooms by RoomRepository.rooms.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 30.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Patient Rooms",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                Button(
                    onClick = { navController.navigate("add_patient") }
                ) {
                    Text("+ Assign a Room")
                }
            }

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
}
