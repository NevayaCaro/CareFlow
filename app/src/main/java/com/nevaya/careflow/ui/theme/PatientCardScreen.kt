package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun PatientCardScreen(
    navController: NavHostController,
    roomNumber: Int
) {

    val fakePatients = listOf(
        Patient("Johnathan Reed", "High Fall Risk", "Assisted", "Regular Diet", "Needs help with mobility.", 7),
        Patient("Maria Lopez", "Moderate Fall Risk", "Independent", "Low Sodium", "Prefers warm blankets.", 5),
        Patient("Sarah Thompson", "Low Fall Risk", "Independent", "Diabetic Diet", "Monitor glucose levels.", 6),
        Patient("James Carter", "High Fall Risk", "Wheelchair", "Pureed Diet", "Requires full assistance.", 9)
    )

    val patient = remember {
        fakePatients.getOrNull((roomNumber / 5) % fakePatients.size)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(60.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            "Patient Information",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Room Number: $roomNumber", fontSize = MaterialTheme.typography.titleMedium.fontSize)
        Text("Name: ${patient?.name ?: "Empty"}")
        Text("Fall Risk: ${patient?.fallRisk ?: "N/A"}")
        Text("Mobility: ${patient?.mobility ?: "N/A"}")
        Text("Diet: ${patient?.diet ?: "N/A"}")
        Text("Notes: ${patient?.notes ?: "N/A"}")
        Text("Care Level: ${patient?.careLevel ?: 0}/10")

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Empty Room")
            }
        }
    }
}