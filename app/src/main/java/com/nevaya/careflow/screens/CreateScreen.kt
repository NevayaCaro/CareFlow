package com.nevaya.careflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.nevaya.careflow.ui.theme.*
import com.nevaya.careflow.data.SessionStore
import com.nevaya.careflow.data.NurseAssignment



@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
    onDoneClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var currentStep by remember { mutableStateOf(1) }
    var assignments by remember { mutableStateOf(listOf<NurseAssignment>()) }

    // Step 2 fields
    var nurseName by remember { mutableStateOf(TextFieldValue("")) }
    var showersText by remember { mutableStateOf(TextFieldValue("")) }
    var mealsText by remember { mutableStateOf(TextFieldValue("")) }
    var editIndex by remember { mutableStateOf<Int?>(null) }
    var selectedRooms by remember { mutableStateOf(setOf<Int>()) }
    var selectedRole by remember { mutableStateOf("CNA") }
    var errorMessage by remember { mutableStateOf("") }


    // Step 3: Rooms
    var firstRoom by remember { mutableStateOf("") }
    var lastRoom by remember { mutableStateOf("") }
    var roomsList by remember { mutableStateOf(listOf<Int>()) }

    val generatedCode = remember { (1000..9999).random().toString() }
    val session = remember(generatedCode) {
        mutableStateOf(SessionStore.createSession(generatedCode))
    }
    var creatorCode by remember { mutableStateOf(TextFieldValue("")) }
    var activeRoomField by remember { mutableStateOf(1) }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        // TOP BACK BUTTON (for all steps except step 1)
        Button(
            onClick = {
                if (currentStep == 1) {
                    onBackClick()
                } else {
                    currentStep = 1
                }
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(10f),
            colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
        ) {
            Text("Back", color = MaterialTheme.colorScheme.onPrimary)
        }
        // CODE TOP RIGHT
        Text(
            text = "CODE: $generatedCode",
            color = TextPrimary,
            fontSize = 22.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .zIndex(10f)
        )

        when (currentStep) {

            // STEP 1: ASSIGN + ROOMS BUTTON
            1 -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //creator code maker
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                        modifier = Modifier
                            .width(320.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Creator Access Code",
                                fontSize = 18.sp,
                                color = TextPrimary
                            )

                            OutlinedTextField(
                                value = creatorCode,
                                onValueChange = {
                                    if (it.text.length <= 4 && it.text.all { c -> c.isDigit() }) {
                                        creatorCode = it
                                    }
                                },
                                label = { Text("Enter 4-digit code") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Text(
                                text = "Make a code to gain access to this created screen when you return from join. Workers use the top-right code to enter worker view, while this code is for creator access only.",
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                        }
                    }
                    // CARD BEHIND BUTTONS
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = GreenPrimary,           // card background
                            contentColor = MaterialTheme.colorScheme.onPrimary // white text/icons inside
                        ),
                        modifier = Modifier
                            .width(320.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            // ROOMS BUTTON
                            Button(
                                onClick = { currentStep = 3 }, // navigate to rooms step
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GreenDark,                // dark green button
                                    contentColor = MaterialTheme.colorScheme.onPrimary // white text
                                ),
                                shape = RoundedCornerShape(50.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(45.dp)
                            ) {
                                Text(text = "Rooms")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // ASSIGN BUTTON
                            Button(
                                onClick = {
                                    nurseName = TextFieldValue("")
                                    showersText = TextFieldValue("")
                                    mealsText = TextFieldValue("")
                                    editIndex = null
                                    currentStep = 2
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GreenDark,                // dark green button
                                    contentColor = MaterialTheme.colorScheme.onPrimary // white text
                                ),
                                shape = RoundedCornerShape(50.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(45.dp)
                            ) {
                                Text(text = "Assign")
                            }

                        }
                    }
                    // ASSIGNMENT LIST
                    assignments.forEachIndexed { index, assignment ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = GreenLight),
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .wrapContentHeight()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(assignment.name, fontSize = 18.sp)
                                Row {
                                    Text(
                                        "Assigned",
                                        color = GreenDark,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )

                                    // EDIT BUTTON
                                    Button(
                                        onClick = {
                                            nurseName = TextFieldValue(assignment.name)
                                            showersText = TextFieldValue(assignment.showers)
                                            mealsText = TextFieldValue(assignment.meals)
                                            editIndex = index
                                            currentStep = 2
                                        },
                                        modifier = Modifier.height(30.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                                    ) {
                                        Text("Edit", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary)
                                    }

                                    Spacer(modifier = Modifier.width(6.dp))

                                    // DELETE BUTTON
                                    Button(
                                        onClick = {
                                            assignments = assignments.toMutableList().also { it.removeAt(index) }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                        modifier = Modifier.height(30.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                    ) {
                                        Text("X", fontSize = 12.sp, color = MaterialTheme.colorScheme.onError)
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            //keeps both codes and adds session data

                            session.value.creatorCode = creatorCode.text
                            session.value.workerCode = session.value.code

                            // navigates to join code
                            onDoneClick()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenDark,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(60.dp)
                    ) {
                        Text("Done")
                    }
                }
            }

            // STEP 3: ROOMS SCREEN
            3 -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // CARD BEHIND INPUTS
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .wrapContentHeight()
                            .padding(vertical = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            // FIRST ROOM
                            OutlinedTextField(
                                value = firstRoom,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("First Room Number", color = TextPrimary) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors()
                            )
                            // LAST ROOM
                            OutlinedTextField(
                                value = lastRoom,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Last Room Number", color = TextPrimary) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Text(
                            text = if (activeRoomField == 1) "Entering First Room" else "Entering Last Room",
                            color = TextPrimary
                        )

                        val buttons = listOf(
                            listOf("1", "2", "3"),
                            listOf("4", "5", "6"),
                            listOf("7", "8", "9"),
                            listOf("DEL", "0", "NEXT")
                        )

                        buttons.forEach { row ->
                            Row {
                                row.forEach { label ->

                                    Button(
                                        onClick = {

                                            val current = if (activeRoomField == 1) firstRoom else lastRoom

                                            when (label) {

                                                "DEL" -> {
                                                    val updated = current.dropLast(1)
                                                    if (activeRoomField == 1) firstRoom = updated else lastRoom = updated
                                                }

                                                "NEXT" -> {
                                                    activeRoomField = if (activeRoomField == 1) 2 else 1
                                                }

                                                else -> {
                                                    if (current.length < 4) {
                                                        val updated = current + label
                                                        if (activeRoomField == 1) firstRoom = updated else lastRoom = updated
                                                    }
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .padding(6.dp)
                                            .size(70.dp)
                                    ) {
                                        Text(label)
                                    }
                                }
                            }
                        }
                    }

                    // CARD BEHIND BUTTONS
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .wrapContentHeight()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // DELETE BUTTON
                            Button(
                                onClick = {
                                    firstRoom = ""
                                    lastRoom = ""
                                    roomsList = emptyList()
                                    activeRoomField = 1
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Delete", color = MaterialTheme.colorScheme.onPrimary)
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // DONE BUTTON
                            Button(
                                onClick = {
                                    val start = firstRoom.toIntOrNull()
                                    val end = lastRoom.toIntOrNull()

                                    roomsList = if (start != null && end != null && end >= start) {
                                        (start..end).toList()
                                    } else {
                                        emptyList()
                                    }

                                    session.value.rooms = roomsList
                                    currentStep = 1
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Done", color = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }

                    // Show current rooms for debug
                    if (roomsList.isNotEmpty()) {
                        Text("Generated rooms: ${roomsList.joinToString(", ")}")
                    }
                }
            }

            // STEP 2 (INPUT SCREEN)
            2 -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 16.dp, end = 16.dp, top = 100.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    //name input and role
                    EditableCard("Nurse/CNA Name", nurseName) { nurseName = it }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text("Role", fontSize = 18.sp, color = TextPrimary)

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                                val roles = listOf("CNA", "LPN", "RN")

                                roles.forEach { role ->

                                    Button(
                                        onClick = { selectedRole = role },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (selectedRole == role) GreenDark else CardBackground
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(role, color = MaterialTheme.colorScheme.onPrimary)
                                    }
                                }
                            }
                        }
                    }

                    //ROOMS SElected
                    if (roomsList.isNotEmpty()) {

                        Card(
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .wrapContentHeight()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                Text("Rooms", fontSize = 18.sp, color = TextPrimary)

                                Spacer(modifier = Modifier.height(12.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                                    roomsList.chunked(3).forEach { rowRooms ->
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                                            rowRooms.forEach { room ->

                                                val isSelected = selectedRooms.contains(room)

                                                Button(
                                                    onClick = {
                                                        selectedRooms = if (isSelected) {
                                                            selectedRooms - room
                                                        } else {
                                                            selectedRooms + room
                                                        }
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = if (isSelected) GreenDark else CardBackground
                                                    ),
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text(
                                                        text = room.toString(),
                                                        color = MaterialTheme.colorScheme.onPrimary
                                                    )
                                                }
                                            }

                                            repeat(3 - rowRooms.size) {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

// SHOWERS SELECTION
                    if (roomsList.isNotEmpty()) {
                        Card(
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .wrapContentHeight()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Showers", fontSize = 18.sp, color = TextPrimary)
                                Spacer(modifier = Modifier.height(12.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    roomsList.chunked(3).forEach { rowRooms ->
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            rowRooms.forEach { room ->
                                                val isSelected = showersText.text.split(", ")
                                                    .mapNotNull { it.toIntOrNull() }
                                                    .contains(room)

                                                Button(
                                                    onClick = {
                                                        val current = showersText.text.split(", ")
                                                            .mapNotNull { it.toIntOrNull() }.toMutableList()
                                                        if (isSelected) current.remove(room) else current.add(room)
                                                        showersText = TextFieldValue(current.sorted().joinToString(", "))
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = if (isSelected) GreenDark else CardBackground
                                                    ),
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text(text = room.toString(), color = MaterialTheme.colorScheme.onPrimary)
                                                }
                                            }
                                            repeat(3 - rowRooms.size) { Spacer(modifier = Modifier.weight(1f)) }
                                        }
                                    }
                                }
                            }
                        }
                    }

// MEALS SELECTION
                    if (roomsList.isNotEmpty()) {
                        Card(
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .wrapContentHeight()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Meals", fontSize = 18.sp, color = TextPrimary)
                                Spacer(modifier = Modifier.height(12.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    roomsList.chunked(3).forEach { rowRooms ->
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            rowRooms.forEach { room ->
                                                val isSelected = mealsText.text.split(", ")
                                                    .mapNotNull { it.toIntOrNull() }
                                                    .contains(room)

                                                Button(
                                                    onClick = {
                                                        val current = mealsText.text.split(", ")
                                                            .mapNotNull { it.toIntOrNull() }.toMutableList()
                                                        if (isSelected) current.remove(room) else current.add(room)
                                                        mealsText = TextFieldValue(current.sorted().joinToString(", "))
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = if (isSelected) GreenDark else CardBackground
                                                    ),
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text(text = room.toString(), color = MaterialTheme.colorScheme.onPrimary)
                                                }
                                            }
                                            repeat(3 - rowRooms.size) { Spacer(modifier = Modifier.weight(1f)) }
                                        }
                                    }
                                }
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {

                            val session = SessionStore.getSession(generatedCode) ?: run {
                                errorMessage = "Session missing"
                                return@Button
                            }

                            val newAssignment = NurseAssignment(
                                name = nurseName.text,
                                role = selectedRole,
                                rooms = selectedRooms.sorted().joinToString(", "),
                                showers = showersText.text,
                                meals = mealsText.text
                            )

                            val updatedAssignments =
                                if (editIndex != null) {
                                    assignments.toMutableList().also {
                                        it[editIndex!!] = newAssignment
                                    }
                                } else {
                                    assignments + newAssignment
                                }

                            session.assignments = updatedAssignments
                            assignments = updatedAssignments

                            currentStep = 1
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenDark,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(60.dp)
                    ) {
                        Text("Done")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableCard(label: String, value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = GreenPrimary, contentColor = MaterialTheme.colorScheme.onPrimary),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, fontSize = 18.sp, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = AppBackground,
                    focusedIndicatorColor = GreenDark,
                    unfocusedIndicatorColor = GreenDark
                )
            )
        }
    }
}