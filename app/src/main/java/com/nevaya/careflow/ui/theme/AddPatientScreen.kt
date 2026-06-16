package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nevaya.careflow.data.model.Patient
import com.nevaya.careflow.data.repository.RoomRepository

@Composable
fun AddPatientScreen(
    navController: NavHostController
) {
    var roomNumber by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var fallRisk by remember { mutableStateOf("") }
    var mobility by remember { mutableStateOf("") }
    var diet by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var medication by remember { mutableStateOf("") }
    var careLevel by remember { mutableStateOf(0) }
    var allergies by remember { mutableStateOf("") }
    var codeStatus by remember { mutableStateOf("") }
    var admissionReason by remember { mutableStateOf("") }
    var assignedNurse by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text("Assign Patient to Room", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = roomNumber,
            onValueChange = { roomNumber = it },
            label = { Text("Room Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Patient Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = fallRisk,
            onValueChange = { fallRisk = it },
            label = { Text("Fall Risk") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = mobility,
            onValueChange = { mobility = it },
            label = { Text("Mobility") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = diet,
            onValueChange = { diet = it },
            label = { Text("Diet") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        OutlinedTextField(
            value = medication,
            onValueChange = { medication = it },
            label = { Text("Medication History") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        OutlinedTextField(
            value = allergies,
            onValueChange = { allergies = it },
            label = { Text("Allergies") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = codeStatus,
            onValueChange = { codeStatus = it },
            label = { Text("Code Status (Full Code / DNR)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = admissionReason,
            onValueChange = { admissionReason = it },
            label = { Text("Admission Reason") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = assignedNurse,
            onValueChange = { assignedNurse = it },
            label = { Text("Assigned Nurse") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
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
                onClick = {
                    val patient = Patient(
                        name = name,
                        fallRisk = fallRisk,
                        mobility = mobility,
                        diet = diet,
                        notes = notes,
                        medication = medication,
                        careLevel = careLevel,
                        allergies = allergies,
                        codeStatus = codeStatus,
                        admissionReason = admissionReason,
                        assignedNurse = assignedNurse
                    )

                    RoomRepository.assignPatient(roomNumber.toInt(), patient)
                    navController.popBackStack()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save Patient")
            }
        }

    }
}
