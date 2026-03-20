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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevaya.careflow.ui.theme.*

// Nurse assignment data
data class NurseAssignment(
    var name: String,
    var rooms: String,
    var showers: String,
    var meals: String
)

@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    var currentStep by remember { mutableStateOf(1) }
    var assignments by remember { mutableStateOf(listOf<NurseAssignment>()) }

    // Step 2 fields
    var nurseName by remember { mutableStateOf(TextFieldValue("")) }
    var roomsText by remember { mutableStateOf(TextFieldValue("")) }
    var showersText by remember { mutableStateOf(TextFieldValue("")) }
    var mealsText by remember { mutableStateOf(TextFieldValue("")) }
    var editIndex by remember { mutableStateOf<Int?>(null) }

    // Step 3: Rooms
    var firstRoom by remember { mutableStateOf(TextFieldValue("")) }
    var lastRoom by remember { mutableStateOf(TextFieldValue("")) }
    var roomsList by remember { mutableStateOf(listOf<Int>()) }

    val generatedCode = remember { (1000..9999).random().toString() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(16.dp)
    ) {

        // TOP BACK BUTTON (for all steps except step 1)
        Button(
            onClick = {
                if (currentStep == 1) {
                    // Step 1: go back to CreateJoinScreen.kt
                    onBackClick()
                } else {
                    // Step 2 or 3: go back to Step 1
                    currentStep = 1
                }
            },
            modifier = Modifier.align(Alignment.TopStart),
            colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
        ) {
            Text("Back", color = MaterialTheme.colorScheme.onPrimary)
        }
        // CODE TOP RIGHT
        Text(
            text = "CODE: $generatedCode",
            color = TextPrimary,
            fontSize = 22.sp,
            modifier = Modifier.align(Alignment.TopEnd)
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
                                    roomsText = TextFieldValue("")
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
                                            roomsText = TextFieldValue(assignment.rooms)
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
                                onValueChange = { newValue ->
                                    if (newValue.text.all { c -> c.isDigit() }) firstRoom = newValue
                                },
                                label = { Text("First Room Number", color = TextPrimary) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()

                            ) // LAST ROOM
                            OutlinedTextField(
                                value = lastRoom,
                                onValueChange = { newValue ->
                                    if (newValue.text.all { c -> c.isDigit() }) lastRoom = newValue
                                },
                                label = { Text("Last Room Number", color = TextPrimary) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
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
                                    firstRoom = TextFieldValue("")
                                    lastRoom = TextFieldValue("")
                                    roomsList = emptyList()
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
                                    val start = firstRoom.text.toIntOrNull()
                                    val end = lastRoom.text.toIntOrNull()
                                    roomsList = if (start != null && end != null && end >= start) {
                                        (start..end).toList()
                                    } else emptyList()
                                    currentStep = 1
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Done", color = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }

                    // OPTIONAL: Show current rooms for debug
                    if (roomsList.isNotEmpty()) {
                        Text("Rooms: ${roomsList.joinToString(", ")}", color = TextPrimary)
                    }
                }
            }

            // STEP 2 (INPUT SCREEN)
            2 -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    EditableCard("Nurse/CNA Name", nurseName) { nurseName = it }
                    EditableCard("Rooms", roomsText) { roomsText = it }
                    EditableCard("Showers", showersText) { showersText = it }
                    EditableCard("Meals", mealsText) { mealsText = it }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val newAssignment = NurseAssignment(
                                name = nurseName.text,
                                rooms = roomsText.text,
                                showers = showersText.text,
                                meals = mealsText.text
                            )

                            if (editIndex != null) {
                                assignments = assignments.toMutableList().also {
                                    it[editIndex!!] = newAssignment
                                }
                            } else {
                                assignments = assignments + newAssignment
                            }

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