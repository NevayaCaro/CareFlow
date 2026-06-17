package com.nevaya.careflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.nevaya.careflow.data.*
import com.nevaya.careflow.ui.theme.GreenPrimary
import com.nevaya.careflow.ui.theme.GreenDark

@Composable
fun CreatorAssignmentScreen(
    sessionCode: String,
    onBack: () -> Unit
) {
    val session = SessionStore.getSessionByCreatorCode(sessionCode)
        ?: SessionStore.getSession(sessionCode)
        ?: return

    var screenMode by remember { mutableStateOf("MAIN") }

    var showAddMember by remember { mutableStateOf(false) }
    var editIndex by remember { mutableStateOf<Int?>(null) }

    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("CNA") }
    var selectedRooms by remember { mutableStateOf(setOf<Int>()) }
    var showersText by remember { mutableStateOf("") }
    var mealsText by remember { mutableStateOf("") }

    val assignments = remember { session.assignments.toMutableStateList() }
    val roomList = session.rooms

    var showPatientForm by remember { mutableStateOf(false) }
    var selectedPatientRoom by remember { mutableStateOf(roomList.firstOrNull() ?: 0) }
    var patientEditIndex by remember { mutableStateOf<Int?>(null) }

    var patientName by remember { mutableStateOf("") }
    var patientAge by remember { mutableStateOf("") }
    var patientGender by remember { mutableStateOf("") }
    var patientDiagnosis by remember { mutableStateOf("") }
    var patientAllergies by remember { mutableStateOf("") }
    var patientDiet by remember { mutableStateOf("") }
    var patientMobility by remember { mutableStateOf("") }
    var patientPrecautions by remember { mutableStateOf("") }
    var patientCodeStatus by remember { mutableStateOf("") }
    var patientNotes by remember { mutableStateOf("") }

    fun resetForm() {
        name = ""
        role = "CNA"
        selectedRooms = emptySet()
        showersText = ""
        mealsText = ""
        editIndex = null
    }

    fun resetPatientForm() {
        patientName = ""
        patientAge = ""
        patientGender = ""
        patientDiagnosis = ""
        patientAllergies = ""
        patientDiet = ""
        patientMobility = ""
        patientPrecautions = ""
        patientCodeStatus = ""
        patientNotes = ""
        patientEditIndex = null
    }

    if (screenMode == "MAIN") {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F6F8))
        ) {

            // ================= TOP BAR =================
            Surface(
                color = GreenPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            null,
                            tint = Color.White
                        )
                    }

                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Creator Dashboard",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "Session Code: ${session.code}",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Creator Code: ${session.creatorCode}",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            Spacer(Modifier.height(10.dp))

            // ================= LIVE ACTIVITY =================
            Text(
                "Live Activity",
                modifier = Modifier.padding(16.dp),
                color = GreenDark
            )

            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {

                val activities = session.liveActivities.reversed()

                Column(
                    Modifier
                        .padding(12.dp)
                        .heightIn(max = 180.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    if (activities.isEmpty()) {
                        Text("No activity yet")
                    } else {
                        activities.forEach {
                            Column {
                                Text(it.workerName, color = GreenPrimary)
                                Text("Room ${it.roomId}")
                                Text(it.message)
                            }
                        }
                    }
                }
            }

            // ================= PATIENT BUTTON (NOW BETWEEN SECTIONS) =================
            Button(
                onClick = { screenMode = "PATIENT" },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(GreenPrimary)
            ) {
                Text("Patient Rooms")
            }

            // ================= ADD MEMBER =================
            Button(
                onClick = {
                    showAddMember = !showAddMember
                    resetForm()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(GreenPrimary)
            ) {
                Text("+ Add New Member")
            }

            // ================= ADD MEMBER FORM =================
            if (showAddMember) {

                Card(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {

                    Column(Modifier.padding(16.dp)) {

                        OutlinedTextField(name, { name = it }, label = { Text("Name") })

                        Row {
                            listOf("CNA", "LPN", "RN").forEach {
                                Button(
                                    onClick = { role = it },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        if (role == it) GreenPrimary else Color.LightGray
                                    )
                                ) { Text(it) }
                            }
                        }

                        Text("Rooms")

                        roomList.chunked(3).forEach { row ->
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                row.forEach { room ->
                                    val selected = selectedRooms.contains(room)

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(
                                                if (selected) GreenPrimary else Color.LightGray,
                                                RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                selectedRooms =
                                                    if (selected) selectedRooms - room
                                                    else selectedRooms + room
                                            }
                                            .padding(10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(room.toString())
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        OutlinedTextField(showersText, { showersText = it }, label = { Text("Showers") })
                        OutlinedTextField(mealsText, { mealsText = it }, label = { Text("Meals") })

                        Button(
                            onClick = {
                                val new = NurseAssignment(
                                    name,
                                    role,
                                    selectedRooms.joinToString(", "),
                                    showersText,
                                    mealsText
                                )

                                if (editIndex != null) assignments[editIndex!!] = new
                                else assignments.add(new)

                                session.assignments = assignments
                                resetForm()
                                showAddMember = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(GreenDark)
                        ) {
                            Text("Save")
                        }
                    }
                }
            }

            // ================= WORKER LIST =================
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(assignments) { worker ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = GreenPrimary)
                    ) {

                        Column(Modifier.padding(16.dp)) {

                            Text(worker.name, color = Color.White)
                            Text(worker.role, color = Color.White)

                            Text("Rooms: ${worker.rooms}", color = Color.White)
                            Text("Showers: ${worker.showers}", color = Color.White)
                            Text("Meals: ${worker.meals}", color = Color.White)

                            Row {
                                Button(
                                    onClick = {
                                        name = worker.name
                                        role = worker.role
                                        selectedRooms = worker.rooms
                                            .split(",")
                                            .mapNotNull { it.trim().toIntOrNull() }
                                            .toSet()

                                        showersText = worker.showers
                                        mealsText = worker.meals
                                        editIndex = assignments.indexOf(worker)
                                        showAddMember = true
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Edit")
                                }

                                Button(
                                    onClick = {
                                        assignments.remove(worker)
                                        session.assignments = assignments
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(Color.Red)
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ================= PATIENT MODE =================
    if (screenMode == "PATIENT") {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F6F8))
        ) {

            Button(
                onClick = {
                    screenMode = "MAIN"
                    showPatientForm = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("← Back")
            }

            if (!showPatientForm) {

                roomList.chunked(3).forEach { row ->

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        row.forEach { room ->

                            val patient = session.patients.find { it.roomNumber == room }

                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {

                                        selectedPatientRoom = room

                                        if (patient != null) {
                                            patientName = patient.patientName
                                            patientAge = patient.age
                                            patientGender = patient.gender
                                            patientDiagnosis = patient.diagnosis
                                            patientAllergies = patient.allergies
                                            patientDiet = patient.diet
                                            patientMobility = patient.mobility
                                            patientPrecautions = patient.precautions
                                            patientCodeStatus = patient.codeStatus
                                            patientNotes = patient.notes
                                            patientEditIndex = session.patients.indexOf(patient)
                                        } else {
                                            resetPatientForm()
                                        }

                                        showPatientForm = true
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor =
                                        if (patient != null) GreenPrimary else Color.LightGray
                                )
                            ) {
                                Column(
                                    Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("Room $room", color = Color.White)
                                    Text(patient?.patientName ?: "Empty", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            if (showPatientForm) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    Button(onClick = { showPatientForm = false }) {
                        Text("← Back to Rooms")
                    }

                    Text("Room $selectedPatientRoom")

                    OutlinedTextField(patientName, { patientName = it }, label = { Text("Name") })
                    OutlinedTextField(patientAge, { patientAge = it }, label = { Text("Age") })
                    OutlinedTextField(patientGender, { patientGender = it }, label = { Text("Gender") })
                    OutlinedTextField(patientDiagnosis, { patientDiagnosis = it }, label = { Text("Diagnosis") })
                    OutlinedTextField(patientAllergies, { patientAllergies = it }, label = { Text("Allergies") })
                    OutlinedTextField(patientDiet, { patientDiet = it }, label = { Text("Diet") })
                    OutlinedTextField(patientMobility, { patientMobility = it }, label = { Text("Mobility") })
                    OutlinedTextField(patientPrecautions, { patientPrecautions = it }, label = { Text("Precautions") })
                    OutlinedTextField(patientCodeStatus, { patientCodeStatus = it }, label = { Text("Code Status") })
                    OutlinedTextField(patientNotes, { patientNotes = it }, label = { Text("Notes") })

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val patient = Patient(
                                roomNumber = selectedPatientRoom,
                                patientName = patientName,
                                age = patientAge,
                                gender = patientGender,
                                diagnosis = patientDiagnosis,
                                allergies = patientAllergies,
                                diet = patientDiet,
                                mobility = patientMobility,
                                precautions = patientPrecautions,
                                codeStatus = patientCodeStatus,
                                notes = patientNotes
                            )

                            if (patientEditIndex != null)
                                session.patients[patientEditIndex!!] = patient
                            else
                                session.patients.add(patient)

                            showPatientForm = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(GreenPrimary)
                    ) {
                        Text("Save Patient")
                    }

                    Button(
                        onClick = {
                            val existing = session.patients.find {
                                it.roomNumber == selectedPatientRoom
                            }

                            if (existing != null) {
                                session.patients.remove(existing)
                                session.liveActivities.add(
                                    LiveActivity(
                                        workerName = "System",
                                        roomId = selectedPatientRoom.toString(),
                                        message = "Patient discharged"
                                    )
                                )
                            }

                            showPatientForm = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color.Red)
                    ) {
                        Text("Discharge")
                    }
                }
            }
        }
    }
}