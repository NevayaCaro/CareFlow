package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nevaya.careflow.ui.theme.CareFlowTheme

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

        Text(
            "Assigned Rooms",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Room 204 - Fall Risk", color = MaterialTheme.colorScheme.onBackground)
        Text("Room 210 - Assisted Mobility", color = MaterialTheme.colorScheme.onBackground)
        Text("Room 215 - Medication Support", color = MaterialTheme.colorScheme.onBackground)
    }
}



