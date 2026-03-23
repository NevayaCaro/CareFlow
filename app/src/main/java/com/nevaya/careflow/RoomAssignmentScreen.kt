package com.nevaya.careflow.ui.screens

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
            .padding(45.dp)
    ) {

        Text("Assigned Rooms", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Room 204 - Fall Risk")
        Text("Room 210 - Assisted Mobility")
        Text("Room 215 - Medication Support")
    }
}

