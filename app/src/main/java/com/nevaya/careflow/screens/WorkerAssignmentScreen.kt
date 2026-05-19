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

                // ================= FIXED HEADER =================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "ROOM $room",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "${worker.name} • ${worker.role}",
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // ================= SHOWER STATUS (STAYS TOP VISUALLY) =================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (showerAssigned)
                            Color(0xFFFFC107).copy(alpha = 0.2f)
                        else
                            Color(0xFF4CAF50).copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (showerAssigned)
                            "SHOWER ASSIGNED"
                        else
                            "NO SHOWER ASSIGNED",
                        modifier = Modifier.padding(14.dp),
                        color = if (showerAssigned)
                            Color(0xFFFFA000)
                        else
                            Color(0xFF2E7D32)
                    )
                }

                Spacer(Modifier.height(12.dp))

                // ================= SCROLLABLE CONTENT =================
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {

                    // ================= SECTION: HYGIENE =================
                    SectionCard(title = "HYGIENE") {

                        val hygieneOptions = listOf(
                            "Shower",
                            "Bed bath",
                            "Peri care",
                            "Teeth brushed",
                            "Gown change"
                        )

                        ChipGrid(
                            items = hygieneOptions,
                            selected = { chart.hygiene.contains(it) },
                            onToggle = {
                                if (chart.hygiene.contains(it)) chart.hygiene.remove(it)
                                else chart.hygiene.add(it)
                            }
                        )
                    }

                    SectionCard(title = "LINEN") {

                        val linenOptions = listOf(
                            "Full bed change",
                            "Pillow case",
                            "Bed sheet",
                            "Underpad",
                            "Brief"
                        )

                        ChipGrid(
                            items = linenOptions,
                            selected = { chart.linen.contains(it) },
                            onToggle = {
                                if (chart.linen.contains(it)) chart.linen.remove(it)
                                else chart.linen.add(it)
                            }
                        )
                    }

                    SectionCard(title = "DEVICE") {

                        val deviceOptions = listOf("Brief", "Pad", "Catheter", "None")

                        ChipGridSingle(
                            items = deviceOptions,
                            selected = chart.device,
                            onSelect = {
                                chart.device = if (chart.device == it) null else it
                            }
                        )

                        Spacer(Modifier.height(10.dp))

                        DeviceCommentBox(chart)
                    }

                    SectionCard(title = "MEALS") {

                        MealSection(chart)
                    }

                    SectionCard(title = "TASKS") {

                        TaskSection(chart)
                    }
                }

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

@Composable
fun SelectableChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (selected)
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
            color = if (selected) Color.White else Color.Gray
        )
    }
}
@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(10.dp))

            content()
        }
    }
}
@Composable
fun ChipGrid(
    items: List<String>,
    selected: (String) -> Boolean,
    onToggle: (String) -> Unit
) {
    Column {
        items.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        SelectableChip(
                            text = item,
                            selected = selected(item),
                            onClick = { onToggle(item) }
                        )
                    }
                }

                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}
@Composable
fun ChipGridSingle(
    items: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    Column {
        items.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        SelectableChip(
                            text = item,
                            selected = selected == item,
                            onClick = { onSelect(item) }
                        )
                    }
                }

                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}
@Composable
fun DeviceCommentBox(chart: com.nevaya.careflow.data.RoomChart) {

    val focusManager = LocalFocusManager.current

    var deviceComment by remember(chart) {
        mutableStateOf(chart.deviceComment)
    }

    OutlinedTextField(
        value = deviceComment,
        onValueChange = { deviceComment = it },
        label = { Text("Device comment") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(6.dp))

    Button(
        onClick = {
            chart.deviceComment = deviceComment
            focusManager.clearFocus()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Save")
    }
}
@Composable
fun MealSection(chart: com.nevaya.careflow.data.RoomChart) {

    val focusManager = LocalFocusManager.current

    Column {

        // ================= INTAKE =================
        var intakeText by remember(chart) {
            mutableStateOf(chart.mealIntake)
        }

        OutlinedTextField(
            value = intakeText,
            onValueChange = {
                val filtered = it.filter { ch -> ch.isDigit() }
                intakeText = filtered
                chart.mealIntake = filtered
            },
            label = { Text("Intake (ML)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

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
            Text("Save Intake")
        }

        Spacer(Modifier.height(10.dp))

        // ================= PERCENTAGE =================
        Text("Eaten Percentage", style = MaterialTheme.typography.labelLarge)

        val percentOptions = listOf("0%", "25%", "50%", "75%", "100%")

        var mealPercentage by remember(chart) {
            mutableStateOf(chart.mealPercentage)
        }

        Spacer(Modifier.height(6.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            percentOptions.forEach { percent ->

                val selected = mealPercentage == percent

                Box(
                    modifier = Modifier
                        .background(
                            if (selected) GreenPrimary else Color.LightGray.copy(alpha = 0.2f),
                            RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            mealPercentage =
                                if (mealPercentage == percent) "" else percent

                            chart.mealPercentage = mealPercentage
                        }
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = percent,
                        color = if (selected) Color.White else Color.Gray
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        // ================= OUTPUT =================
        var outputText by remember(chart) {
            mutableStateOf(chart.mealOutputMl)
        }

        OutlinedTextField(
            value = outputText,
            onValueChange = {
                val filtered = it.filter { ch -> ch.isDigit() }
                outputText = filtered
                chart.mealOutputMl = filtered
            },
            label = { Text("Output (ML)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

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
            Text("Save Output")
        }
    }
}
@Composable
fun TaskSection(chart: com.nevaya.careflow.data.RoomChart) {

    var taskInput by remember { mutableStateOf("") }

    Column {

        // ================= ADD TASK =================
        Row {

            OutlinedTextField(
                value = taskInput,
                onValueChange = { taskInput = it },
                modifier = Modifier.weight(1f),
                label = { Text("New task") }
            )

            Spacer(Modifier.width(8.dp))

            Button(
                onClick = {
                    if (taskInput.isNotBlank()) {
                        chart.tasks.add(taskInput.trim())
                        taskInput = ""
                    }
                }
            ) {
                Text("Add")
            }
        }

        Spacer(Modifier.height(10.dp))

        // ================= TASK LIST =================
        if (chart.tasks.isEmpty()) {
            Text(
                text = "No tasks assigned",
                color = Color.Gray
            )
            return
        }

        chart.tasks.forEach { task ->

            val completed = task.startsWith("[DONE]")

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        val index = chart.tasks.indexOf(task)

                        if (completed) {
                            chart.tasks[index] = task.removePrefix("[DONE] ")
                        } else {
                            chart.tasks[index] = "[DONE] $task"
                        }
                    },
                colors = CardDefaults.cardColors(
                    containerColor =
                        if (completed)
                            Color.LightGray.copy(alpha = 0.3f)
                        else
                            Color.White
                )
            ) {
                Text(
                    text = if (completed)
                        "✓ ${task.removePrefix("[DONE] ")}"
                    else
                        task,
                    modifier = Modifier.padding(12.dp),
                    color = if (completed) Color.Gray else Color.Black
                )
            }
        }
    }
}