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
import com.nevaya.careflow.data.SessionStore
import com.nevaya.careflow.data.NurseAssignment
import com.nevaya.careflow.data.RoomChart
import com.nevaya.careflow.ui.theme.GreenPrimary
import com.nevaya.careflow.ui.theme.GreenDark
import com.nevaya.careflow.data.Patient
import com.nevaya.careflow.data.LiveActivity

@Composable
fun CreatorAssignmentScreen(
    sessionCode: String,
    onBack: () -> Unit
) {
    val session = SessionStore.getSessionByCreatorCode(sessionCode)
        ?: SessionStore.getSession(sessionCode)
        ?: run {
            Text("Invalid session code")
            return
        }

    var selectedWorker by remember { mutableStateOf<NurseAssignment?>(null) }
    var showAddMember by remember { mutableStateOf(false) }

    var editIndex by remember { mutableStateOf<Int?>(null) }

    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("CNA") }
    var selectedRooms by remember { mutableStateOf(setOf<Int>()) }
    var showersText by remember { mutableStateOf("") }
    var mealsText by remember { mutableStateOf("") }

    val assignments = session.assignments.toMutableStateList()
    val roomList = session.rooms
    var showPatientForm by remember { mutableStateOf(false) }

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

    var selectedPatientRoom by remember {
        mutableStateOf(roomList.firstOrNull() ?: 0)
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

        selectedPatientRoom = roomList.firstOrNull() ?: 0

        patientEditIndex = null
    }

    fun resetForm() {
        name = ""
        role = "CNA"
        selectedRooms = emptySet()
        showersText = ""
        mealsText = ""
        editIndex = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
    ) {

        // ================= TOP BAR =================
        Surface(color = GreenPrimary) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }

                Text(
                    "Creator Dashboard",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(10.dp))
        // ================= LIVE ACTIVITY FEED =================
        Text(
            text = "Live Activity",
            modifier = Modifier.padding(start = 16.dp, top = 8.dp),
            style = MaterialTheme.typography.titleMedium,
            color = GreenDark
        )

        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            val activities = session.liveActivities.reversed()

            if (activities.isEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No activity yet", color = Color.Gray)
                }
            } else {

                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .heightIn(max = 180.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    activities.forEach { activity ->

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF1F3F5)
                            )
                        ) {
                            Column(Modifier.padding(10.dp)) {

                                Text(
                                    text = activity.workerName,
                                    color = GreenPrimary
                                )

                                Text(
                                    text = "Room ${activity.roomId}",
                                    color = Color.Gray
                                )

                                Text(
                                    text = activity.message,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }

        // ================= ADD MEMBER BUTTON =================
        Button(
            onClick = {
                showAddMember = !showAddMember
                resetForm()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
        ) {
            Text("+ Add New Member", color = Color.White)
        }

        // ================= ADD / EDIT FORM =================
        if (showAddMember) {

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {

                Column(Modifier.padding(16.dp)) {

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(10.dp))

                    Row {
                        listOf("CNA", "LPN", "RN").forEach {
                            Button(
                                onClick = { role = it },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (role == it) GreenPrimary else Color.LightGray
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(it)
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    // ROOMS
                    Text("Rooms")

                    roomList.chunked(4).forEach { row ->
                        Row {
                            row.forEach { room ->

                                val selected = selectedRooms.contains(room)

                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .background(
                                            if (selected) GreenPrimary else Color.LightGray,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable {
                                            selectedRooms =
                                                if (selected) selectedRooms - room
                                                else selectedRooms + room
                                        }
                                        .padding(10.dp)
                                ) {
                                    Text(room.toString())
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = showersText,
                        onValueChange = { showersText = it },
                        label = { Text("Showers (comma)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = mealsText,
                        onValueChange = { mealsText = it },
                        label = { Text("Meals (comma)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = {

                            val new = NurseAssignment(
                                name = name,
                                role = role,
                                rooms = selectedRooms.sorted().joinToString(", "),
                                showers = showersText,
                                meals = mealsText
                            )

                            if (editIndex != null) {
                                assignments[editIndex!!] = new
                            } else {
                                assignments.add(new)
                            }

                            session.assignments = assignments
                            resetForm()
                            showAddMember = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                    ) {
                        Text(if (editIndex == null) "Add Worker" else "Save Changes", color = Color.White)
                    }
                }
            }
        }

        // ================= WORKER LIST (LIKE WORKER SCREEN) =================
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(assignments) { worker ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Column(Modifier.padding(16.dp)) {

                        Text(worker.name, color = Color.White)
                        Text(worker.role, color = Color.White)

                        Spacer(Modifier.height(8.dp))

                        Text("Rooms: ${worker.rooms}", color = Color.White.copy(0.9f))
                        Text("Showers: ${worker.showers}", color = Color.White.copy(0.9f))
                        Text("Meals: ${worker.meals}", color = Color.White.copy(0.9f))

                        Spacer(Modifier.height(10.dp))

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

                            Spacer(Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    assignments.remove(worker)
                                    session.assignments = assignments
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(20.dp))

        Text(
            text = "Patient Rooms",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleLarge,
            color = GreenDark
        )

        Spacer(Modifier.height(12.dp))

        roomList.chunked(3).forEach { row ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                row.forEach { room ->

                    val patient =
                        session.patients.find {
                            it.roomNumber == room
                        }

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

                                    patientEditIndex =
                                        session.patients.indexOf(patient)

                                } else {

                                    resetPatientForm()
                                    selectedPatientRoom = room
                                }

                                showPatientForm = true
                            },
                        colors = CardDefaults.cardColors(
                            containerColor =
                                if (patient != null)
                                    GreenPrimary
                                else
                                    Color.LightGray
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                "Room $room",
                                color = Color.White
                            )

                            Text(
                                patient?.patientName ?: "Empty",
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }

        if (showPatientForm) {

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        "Room $selectedPatientRoom",
                        style = MaterialTheme.typography.titleLarge
                    )

                    OutlinedTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        label = { Text("Patient Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = patientAge,
                        onValueChange = { patientAge = it },
                        label = { Text("Age") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = patientGender,
                        onValueChange = { patientGender = it },
                        label = { Text("Gender") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = patientDiagnosis,
                        onValueChange = { patientDiagnosis = it },
                        label = { Text("Diagnosis") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = patientAllergies,
                        onValueChange = { patientAllergies = it },
                        label = { Text("Allergies") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = patientDiet,
                        onValueChange = { patientDiet = it },
                        label = { Text("Diet") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = patientMobility,
                        onValueChange = { patientMobility = it },
                        label = { Text("Mobility") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = patientPrecautions,
                        onValueChange = { patientPrecautions = it },
                        label = { Text("Precautions") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = patientCodeStatus,
                        onValueChange = { patientCodeStatus = it },
                        label = { Text("Code Status") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = patientNotes,
                        onValueChange = { patientNotes = it },
                        label = { Text("Notes") },
                        modifier = Modifier.fillMaxWidth()
                    )

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

                            if (patientEditIndex != null) {
                                session.patients[patientEditIndex!!] = patient
                            } else {
                                session.patients.add(patient)
                            }

                            showPatientForm = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenPrimary
                        )
                    ) {
                        Text("Save Patient")
                    }

                    Button(
                        onClick = {

                            val existing =
                                session.patients.find {
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
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text("Discharge")
                    }
                }
            }
        }
    }
}

