package com.nevaya.careflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nevaya.careflow.data.SessionStore
import com.nevaya.careflow.data.NurseAssignment
import com.nevaya.careflow.ui.theme.GreenPrimary
import com.nevaya.careflow.ui.theme.GreenDark
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.text.style.TextAlign

@Composable
fun WorkerAssignmentScreen(
    sessionCode: String,
    onBack: () -> Unit
) {

    val session = SessionStore.getSession(sessionCode)

    if (session == null) {
        Text("Invalid session code")
        return
    }

    var selectedWorker by remember { mutableStateOf<NurseAssignment?>(null) }
    var selectedRoom by remember { mutableStateOf<Int?>(null) }

    val assignments = session.assignments


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // TOP BAR
        Surface(color = GreenPrimary, modifier = Modifier.fillMaxWidth()) {
            Box(Modifier.padding(16.dp).fillMaxWidth()) {

                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Shift Board", color = Color.White)
                    Text("Code: ${session.code}", color = Color.White)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // STATE 1: WORKER LIST
        if (selectedWorker == null) {

            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(assignments) { worker ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                            ) {
                                selectedWorker = worker
                            },
                        colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(worker.name, color = Color.White)
                            Text(worker.role, color = Color.White)
                        }
                    }
                }
            }
        }

        // STATE 2: ROOM GRID
        else {

            TextButton(onClick = {
                selectedWorker = null
                selectedRoom = null
            }) {
                Text("← Back", color = GreenDark)
            }

            val worker = selectedWorker!!

            Text(
                text = "${worker.name} (${worker.role})",
                modifier = Modifier.padding(16.dp),
                color = GreenDark,
                style = MaterialTheme.typography.headlineSmall
            )

            // CONVERT STRING → LIST HERE ONLY
            val roomList = remember(worker.rooms) {
                worker.rooms
                    .split(",")
                    .mapNotNull { it.trim().toIntOrNull() }
            }

            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(roomList.chunked(3)) { row ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        row.forEach { room ->

                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(90.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                                    ) {
                                        selectedRoom = room
                                    },
                                colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(room.toString(), color = Color.White)
                                }
                            }
                        }

                        repeat(3 - row.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        //chip
        @Composable
        fun SelectableChip(
            text: String,
            selected: Boolean,
            onClick: () -> Unit
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color =
                            if (selected)
                                GreenPrimary
                            else
                                Color.LightGray.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onClick() }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = text,
                    color =
                        if (selected)
                            Color.White
                        else
                            Color.Gray
                )
            }
        }

        // STATE 3: ROOM DETAILS
        selectedRoom?.let { room ->

            val worker = selectedWorker!!
            val chart = session.roomCharts.getOrPut(room.toString()) {
                com.nevaya.careflow.data.RoomChart(room.toString())
            }

            val showerAssigned = worker.showers
                .split(",")
                .map { it.trim() }
                .contains(room.toString())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Text(
                    text = "ROOM $room",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "SHOWER - " + if (showerAssigned) "ASSIGNED" else "NOT ASSIGNED",
                    color = if (showerAssigned) Color(0xFFFFC107) else Color(0xFF4CAF50)
                )

                Spacer(Modifier.height(12.dp))


                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {

// ================= HYGIENE =================
                    Text("HYGIENE", style = MaterialTheme.typography.titleMedium)

                    Spacer(Modifier.height(8.dp))

                    val hygieneOptions = listOf(
                        "Shower",
                        "Bed bath",
                        "Peri care",
                        "Teeth brushed",
                        "Gown change"
                    )

                    Column {
                        hygieneOptions.chunked(2).forEach { row ->

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                row.forEach { item ->

                                    val selected = chart.hygiene.contains(item)

                                    Box(modifier = Modifier.weight(1f)) {
                                        SelectableChip(
                                            text = item,
                                            selected = selected,
                                            onClick = {
                                                if (selected) chart.hygiene.remove(item)
                                                else chart.hygiene.add(item)
                                            }
                                        )
                                    }
                                }

                                if (row.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }

                            Spacer(Modifier.height(10.dp))
                        }
                    }

                    Spacer(Modifier.height(12.dp))

// ================= LINEN =================
                    Text("LINEN", style = MaterialTheme.typography.titleMedium)

                    Spacer(Modifier.height(8.dp))

                    val linenOptions = listOf(
                        "Full bed change",
                        "Pillow case",
                        "Bed sheet",
                        "Underpad",
                        "Brief"
                    )

                    Column {
                        linenOptions.chunked(2).forEach { row ->

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                row.forEach { item ->

                                    val selected = chart.linen.contains(item)

                                    Box(modifier = Modifier.weight(1f)) {
                                        SelectableChip(
                                            text = item,
                                            selected = selected,
                                            onClick = {
                                                if (selected) chart.linen.remove(item)
                                                else chart.linen.add(item)
                                            }
                                        )
                                    }
                                }

                                if (row.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }

                            Spacer(Modifier.height(10.dp))
                        }
                    }

                    Spacer(Modifier.height(12.dp))
// ================= INCONTINENT DEVICE =================
                    Text(
                        text = "INCONTINENT DEVICE",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    val deviceOptions = listOf("Brief", "Pad", "Catheter", "None")

                    Column {
                        deviceOptions.chunked(2).forEach { row ->

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                row.forEach { item ->

                                    val selected = chart.device == item

                                    Box(modifier = Modifier.weight(1f)) {

                                        SelectableChip(
                                            text = item,
                                            selected = selected,
                                            onClick = {
                                                chart.device =
                                                    if (selected) null else item
                                            }
                                        )
                                    }
                                }

                                if (row.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }

                            Spacer(Modifier.height(10.dp))
                        }
                    }

                    Spacer(Modifier.height(12.dp))

// ================= DEVICE COMMENT =================
                    val focusManager = LocalFocusManager.current

                    var deviceComment by remember(room) {
                        mutableStateOf(chart.deviceComment)
                    }
                    OutlinedTextField(
                        value = deviceComment,
                        onValueChange = { deviceComment = it },
                        label = { Text("Device comment") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                chart.deviceComment = deviceComment
                                focusManager.clearFocus()
                            }
                        ),
                        singleLine = false
                    )

                    Spacer(Modifier.height(8.dp))

// ================= REAL VISIBLE DONE BUTTON =================
                    Button(
                        onClick = {
                            chart.deviceComment = deviceComment
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Done")
                    }

                    Spacer(Modifier.height(12.dp))


// ================= MEALS =================
                    Text("MEALS", style = MaterialTheme.typography.titleMedium)

                    Spacer(Modifier.height(6.dp))

                    val mealsAssigned =
                        chart.mealIntake.isNotBlank() ||
                                chart.mealOutputMl.isNotBlank() ||
                                chart.mealPercentage.isNotBlank()

                    Text(
                        text = "MEALS - " + if (mealsAssigned) "ASSIGNED" else "NOT ASSIGNED",
                        color = if (mealsAssigned)
                            Color(0xFFFFC107)
                        else
                            Color(0xFF4CAF50)
                    )

// ================= INTAKE ML =================
                    var intakeText by remember(room) {
                        mutableStateOf(chart.mealIntake)
                    }
                    OutlinedTextField(
                        value = intakeText,
                        onValueChange = { input ->
                            val filtered = input.filter { it.isDigit() }
                            intakeText = filtered
                            chart.mealIntake = filtered
                        },
                        label = { Text("Intake ML (1 - 100000)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    Button(
                        onClick = {
                            chart.mealIntake =
                                intakeText.toIntOrNull()
                                    ?.coerceIn(1, 100000)
                                    ?.toString()
                                    ?: ""
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Done")
                    }

                    Spacer(Modifier.height(8.dp))

// ================= PERCENTAGE TOGGLE =================
                    val percentOptions = listOf("0%", "25%", "50%", "75%", "100%")

                    var mealPercentage by remember(room) {
                        mutableStateOf(chart.mealPercentage)
                    }
                    Row {
                        percentOptions.forEach { percent ->

                            val selected = mealPercentage == percent

                            Text(
                                text = percent,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .background(
                                        if (selected)
                                            GreenPrimary.copy(alpha = 0.2f)
                                        else Color.Transparent
                                    )
                                    .clickable {
                                        mealPercentage =
                                            if (mealPercentage == percent) "" else percent

                                        chart.mealPercentage = mealPercentage
                                    }
                                    .padding(6.dp),
                                color = if (selected) GreenPrimary else Color.Gray
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))

// ================= OUTPUT ML =================
                    var outputText by remember(room) {
                        mutableStateOf(chart.mealOutputMl)
                    }
                    OutlinedTextField(
                        value = outputText,
                        onValueChange = { input ->
                            val filtered = input.filter { it.isDigit() }
                            outputText = filtered
                            chart.mealOutputMl = filtered
                        },
                        label = { Text("Output ML") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    Button(
                        onClick = {
                            chart.mealOutputMl =
                                outputText.toIntOrNull()
                                    ?.coerceAtLeast(0)
                                    ?.toString()
                                    ?: ""
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Done")
                    }

                    Spacer(Modifier.height(8.dp))

                    // ================= TASKS =================
                    Text("TASKS", style = MaterialTheme.typography.titleMedium)

                    var taskInput by remember { mutableStateOf("") }

                    Row {
                        OutlinedTextField(
                            value = taskInput,
                            onValueChange = { taskInput = it },
                            modifier = Modifier.weight(1f),
                            label = { Text("Add task") }
                        )

                        Button(onClick = {
                            if (taskInput.isNotBlank()) {
                                chart.tasks.add(taskInput)
                                taskInput = ""
                            }
                        }) {
                            Text("Add")
                        }
                    }

                    chart.tasks.forEach { task ->

                        Text(
                            text = "• $task",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    chart.tasks.remove(task)
                                }
                                .padding(8.dp),
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { selectedRoom = null },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close", color = Color.White)
                }
            }
        }
    }
}