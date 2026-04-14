package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(padding: PaddingValues) {

    var notificationsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(50.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // HEADER
        Text(
            "Preferences",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary   // DARK GREEN
        )

        Spacer(modifier = Modifier.height(20.dp))

        // NOTIFICATION TOGGLE
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "Enable Notifications",
                    color = MaterialTheme.colorScheme.onSurface
                )

                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.secondary,
                        checkedTrackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    )
                )
            }
        }
    }
}

