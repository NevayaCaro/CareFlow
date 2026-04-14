package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomAssignmentsScreen(padding: PaddingValues) {

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(50.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // HEADER
        Text(
            "Assigned Rooms",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary   // DARK GREEN
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ROOM 1
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                "Room 204 – Fall Risk",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ROOM 2
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                "Room 210 – Assisted Mobility",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ROOM 3
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                "Room 215 – Medication Support",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


