package com.nevaya.careflow.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateJoinScreen(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "CareFlow",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { /* TODO: Navigate to Create Shift */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Shift")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { /* TODO: Navigate to Join Shift */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Join Shift")
        }
    }
}