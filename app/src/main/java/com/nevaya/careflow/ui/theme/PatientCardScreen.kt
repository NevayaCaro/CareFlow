package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nevaya.careflow.viewmodels.PatientCardViewModel

@Composable
fun PatientCardScreen(
    navController: NavHostController,
    roomNumber: Int,
    viewModel: PatientCardViewModel = PatientCardViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(roomNumber) {
        viewModel.loadRoom(roomNumber)
    }

    val roomState by viewModel.roomState.collectAsState()
    val patient = roomState?.patient

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

        Text("Room Number: $roomNumber")
        Text("Name: ${patient?.name ?: "Empty"}")
        Text("Fall Risk: ${patient?.fallRisk ?: "N/A"}")
        Text("Mobility: ${patient?.mobility ?: "N/A"}")
        Text("Diet: ${patient?.diet ?: "N/A"}")
        Text("Notes: ${patient?.notes ?: "N/A"}")
        Text("Medication: ${patient?.medication ?: "N/A"}")
        Text("Care Level: ${patient?.careLevel ?: 0}/10")
        Text("Allergies: ${patient?.allergies ?: "N/A"}")
        Text("Code Status: ${patient?.codeStatus ?: "N/A"}")
        Text("Admission Reason: ${patient?.admissionReason ?: "N/A"}")
        Text("Assigned Nurse: ${patient?.assignedNurse ?: "N/A"}")


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
                onClick = { showDialog = true },
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

    // Confirmation Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Room Clear") },
            text = { Text("Are you sure you want to empty this room? This will remove all patient information.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearRoom(roomNumber)
                        showDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}
