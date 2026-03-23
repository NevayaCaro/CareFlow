package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(padding: PaddingValues) {

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(45.dp)
    ) {

        Text("Team Messages", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Nurse Admin: Patient in Room 204 needs attention")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Supervisor: Shift change updated")
    }
}

