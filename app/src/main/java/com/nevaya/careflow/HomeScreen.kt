package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    padding: PaddingValues,
    onViewSchedule: () -> Unit = {},
    onViewAssignments: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(45.dp)
    ) {

        Text(
            text = "Welcome Back 👋",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onViewSchedule,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Schedule")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onViewAssignments,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Room Assignments")
        }
    }
}