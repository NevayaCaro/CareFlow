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

// This is like a little box for each nurse's info
data class NurseAssignment(
    var name: String,
    var rooms: String,
    var showers: String,
    var meals: String
)

@Composable
fun CreateScreen(modifier: Modifier = Modifier) {
    var currentStep by remember { mutableStateOf(1) } // 1=assign button, 2=typing info
    var assignments by remember { mutableStateOf(listOf<NurseAssignment>()) } // list of nurses

    // Temporary spots to type stuff
    var nurseName by remember { mutableStateOf(TextFieldValue("")) }
    var roomsText by remember { mutableStateOf(TextFieldValue("")) }
    var showersText by remember { mutableStateOf(TextFieldValue("")) }
    var mealsText by remember { mutableStateOf(TextFieldValue("")) }

    var editIndex by remember { mutableStateOf<Int?>(null) } // which nurse we are fixing

    val generatedCode = remember { (1000..9999).random().toString() } // secret 4-digit code

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFBFC9D1)) // gray screen
            .padding(16.dp)
    ) {
        // Top right code
        Text(
            text = "CODE: $generatedCode",
            color = Color.White,
            fontSize = 22.sp,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        // Shows secret code all the time

        when (currentStep) {
            1 -> {
                // ASSIGN SCREEN
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .verticalScroll(rememberScrollState()), // scroll if too long
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            // clear old typing stuff
                            nurseName = TextFieldValue("")
                            roomsText = TextFieldValue("")
                            showersText = TextFieldValue("")
                            mealsText = TextFieldValue("")
                            editIndex = null
                            currentStep = 2 // go type nurse info
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(60.dp)
                    ) {
                        Text("Assign") // big button to type info
                    }

                    // LIST OF NURSES
                    assignments.forEachIndexed { index, assignment ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(assignment.name, fontSize = 18.sp) // show nurse name

                            Row {
                                Text(
                                    "Assigned",
                                    color = Color(0xFF6A1B9A), // make assigned purple
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(end = 12.dp)
                                )

                                Button(
                                    onClick = {
                                        // load old info to edit
                                        nurseName = TextFieldValue(assignment.name)
                                        roomsText = TextFieldValue(assignment.rooms)
                                        showersText = TextFieldValue(assignment.showers)
                                        mealsText = TextFieldValue(assignment.meals)
                                        editIndex = index
                                        currentStep = 2 // go to typing screen
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF0288D1) // blue edit button
                                    ),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text("Edit", color = Color.White, fontSize = 14.sp)
                                }
                                // edit button to fix nurse info
                            }
                        }
                    }
                }
            }

            2 -> {
                // EDIT / TYPE NURSE INFO SCREEN
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .verticalScroll(rememberScrollState()), // scroll if too big
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EditableCard("Nurse/CNA Name", nurseName) { nurseName = it } // box to type nurse
                    EditableCard("Rooms", roomsText) { roomsText = it } // type rooms
                    EditableCard("Showers", showersText) { showersText = it } // type showers
                    EditableCard("Meals", mealsText) { mealsText = it } // type meals

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            // save new or edited nurse
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
                            currentStep = 1 // go back to assign screen
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(60.dp)
                    ) {
                        Text("Done") // done button saves and returns
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableCard(label: String, value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    // card box for typing stuff
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, fontSize = 18.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = value, // letters you type
                onValueChange = onValueChange, // change when typing
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp), // big enough for typing
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF0F0F0), // light gray background
                    focusedIndicatorColor = Color.Gray, // line when typing
                    unfocusedIndicatorColor = Color.LightGray // line when not typing
                )
            )
            // here is where you type the stuff
            // the placeholder shows what to type
            // letters fit inside box
        }
    }
}