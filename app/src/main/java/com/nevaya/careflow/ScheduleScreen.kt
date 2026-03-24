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
fun ScheduleScreen(padding: PaddingValues) {

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(50.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Text(
            "Your Upcoming Shifts",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("March 18 • 7:00 AM - 3:00 PM", color = MaterialTheme.colorScheme.onBackground)
        Text("March 19 • 3:00 PM - 11:00 PM", color = MaterialTheme.colorScheme.onBackground)
        Text("March 21 • 7:00 AM - 3:00 PM", color = MaterialTheme.colorScheme.onBackground)
    }
}


