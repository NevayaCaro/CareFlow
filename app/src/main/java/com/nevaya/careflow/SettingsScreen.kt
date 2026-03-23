package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(padding: PaddingValues) {

    var notificationsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(45.dp)
    ) {

        Text("Preferences", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Enable Notifications")
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
        }
    }
}