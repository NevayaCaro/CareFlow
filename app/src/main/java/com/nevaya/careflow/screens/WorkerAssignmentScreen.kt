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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalFocusManager
import com.nevaya.careflow.data.RoomChart
import com.nevaya.careflow.data.RoomChartSnapshot
import com.nevaya.careflow.data.LiveActivity

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
    var viewMode by remember { mutableStateOf(false) }
    var selectedSnapshot by remember { mutableStateOf<RoomChartSnapshot?>(null) }
    var showSavedEntries by remember { mutableStateOf(false) }

    val assignments = session.assignments


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // TOP BAR
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
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Worker Assignment",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "Code: ${session.code}",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
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

        if (viewMode && selectedSnapshot != null) {

            val c = selectedSnapshot!!.chart

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Text(
                    text = "ROOM ${selectedSnapshot!!.roomId} SUMMARY",
                    style = MaterialTheme.typography.headlineSmall,
                    color = GreenDark
                )

                Spacer(Modifier.height(12.dp))

                Text("Hygiene: ${if (c.hygiene.isEmpty()) "None" else c.hygiene.joinToString(", ")}")

                Spacer(Modifier.height(8.dp))

                Text("Linen: ${if (c.linen.isEmpty()) "None" else c.linen.joinToString(", ")}")

                Spacer(Modifier.height(8.dp))

                Text("Device: ${c.device ?: "None"}")

                Spacer(Modifier.height(8.dp))

                Text("Intake: ${c.mealIntake}")

                Text("Output: ${c.mealOutputMl}")

                Text("Eaten: ${c.mealPercentage}")

                Spacer(Modifier.height(8.dp))

                Text(
                    "Tasks: ${
                        if (c.tasks.isEmpty())
                            "None"
                        else
                            c.tasks.joinToString(", ")
                    }"
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        selectedSnapshot = null
                        viewMode = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back")
                }
            }

            return
        }

        if (showSavedEntries) {

            val savedForRoom = session.savedRoomCharts
                .filter { it.roomId == selectedRoom?.toString() }
                .reversed()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Text(
                    "Saved Entries",
                    style = MaterialTheme.typography.headlineSmall,
                    color = GreenDark
                )

                Spacer(Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(savedForRoom) { snapshot ->

                        val c = snapshot.chart

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedSnapshot = snapshot
                                    viewMode = true
                                    showSavedEntries = false
                                }
                        ) {
                            Column(Modifier.padding(12.dp)) {

                                Text("Room ${snapshot.roomId}")

                                Spacer(Modifier.height(6.dp))

                                Text("Hygiene: ${if (c.hygiene.isEmpty()) "None" else c.hygiene.joinToString(", ")}")

                                if (c.hygieneOther.isNotEmpty()) {
                                    Text("Hygiene Other: ${c.hygieneOther.joinToString(", ")}")
                                }

                                Spacer(Modifier.height(4.dp))

                                Text("Linen: ${if (c.linen.isEmpty()) "None" else c.linen.joinToString(", ")}")

                                if (c.linenOther.isNotEmpty()) {
                                    Text("Linen Other: ${c.linenOther.joinToString(", ")}")
                                }

                                Spacer(Modifier.height(4.dp))

                                Text("Device: ${c.device ?: "None"}")

                                if (c.deviceOther.isNotEmpty()) {
                                    Text("Device Other: ${c.deviceOther.joinToString(", ")}")
                                }

                                Spacer(Modifier.height(6.dp))

                                Text("Tasks: ${
                                    if (c.tasks.isEmpty()) "None"
                                    else c.tasks.joinToString(", ")
                                }")

                                Spacer(Modifier.height(6.dp))

                                Text("Tap to view details", color = Color.Gray)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        showSavedEntries = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back")
                }
            }

            return
        }

        // STATE 3: ROOM DETAILS

        selectedRoom?.let { room ->

            val roomKey = room.toString()
            val worker = selectedWorker ?: return@let


            val chart = remember(roomKey) {
                session.roomCharts.getOrPut(roomKey) {
                    RoomChart(roomKey)
                }
            }

            val showerAssigned = worker.showers
                .split(",")
                .map { it.trim() }
                .contains(room.toString())

            val mealAssigned = worker.meals
                .split(",")
                .map { it.trim() }
                .contains(room.toString())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                // HEADER
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("ROOM $room", color = Color.White)
                        Text("${worker.name} • ${worker.role}", color = Color.White.copy(alpha = 0.85f))
                    }
                }

                Spacer(Modifier.height(12.dp))

                // SHOWER STATUS
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (showerAssigned)
                            Color(0xFFFFC107).copy(alpha = 0.2f)
                        else
                            Color(0xFF4CAF50).copy(alpha = 0.15f)
                    )
                ) {
                    Text(
                        text = if (showerAssigned) "SHOWER ASSIGNED" else "NO SHOWER ASSIGNED",
                        modifier = Modifier.padding(14.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (mealAssigned)
                                Color(0xFF4CAF50).copy(alpha = 0.15f)
                            else
                                Color.Red.copy(alpha = 0.12f)
                    )
                ) {
                    Text(
                        text =
                            if (mealAssigned)
                                "MEAL ASSIGNED"
                            else
                                "NO MEAL ASSIGNED",
                        modifier = Modifier.padding(14.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))

                // CONTENT
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {

                    SectionCard("HYGIENE") {

                        val options = listOf(
                            "Shower",
                            "Bed bath",
                            "Peri care",
                            "Teeth brushed",
                            "Gown change",
                            "Other"
                        )

                        ChipGrid(
                            items = options,
                            selected = { chart.hygiene.contains(it) },
                            onToggle = {

                                if (it == "Other") {
                                    chart.hygiene.add("Other")
                                }
                                else if (chart.hygiene.contains(it)) {
                                    chart.hygiene.remove(it)
                                }
                                else {
                                    chart.hygiene.add(it)
                                }
                            }
                        )


                        if (chart.hygiene.contains("Other")) {
                            OtherCommentBox(
                                title = "Hygiene Other",
                                items = chart.hygieneOther
                            )
                        }
                    }

                    SectionCard("LINEN") {

                        val options = listOf(
                            "Full bed change",
                            "Pillow case",
                            "Bed sheet",
                            "Underpad",
                            "Brief",
                            "Other"
                        )

                        ChipGrid(
                            items = options,
                            selected = { chart.linen.contains(it) },
                            onToggle = {

                                if (chart.linen.contains(it)) {
                                    chart.linen.remove(it)
                                }
                                else {
                                    chart.linen.add(it)
                                }
                            }
                        )


                        if (chart.linen.contains("Other")) {

                            OtherCommentBox(
                                title = "Linen Other",
                                items = chart.linenOther
                            )
                        }
                    }

                    SectionCard("DEVICE") {

                        var deviceState by remember(roomKey) {
                            mutableStateOf(chart.device)
                        }

                        val options = listOf(
                            "Brief",
                            "Pad",
                            "Catheter",
                            "None",
                            "Other"
                        )


                        ChipGridSingle(
                            items = options,
                            selected = deviceState,
                            onSelect = {

                                deviceState =
                                    if (deviceState == it)
                                        null
                                    else
                                        it

                                chart.device = deviceState
                            }
                        )


                        if (deviceState == "Other") {

                            OtherCommentBox(
                                title = "Device Other",
                                items = chart.deviceOther
                            )
                        }
                    }

                    SectionCard("MEALS") {
                        MealSection(chart)
                    }

                    SectionCard("TASKS") {
                        TaskSection(chart)
                    }
                }

                // SAVE
                Button(
                    onClick = {

                        val currentChart = session.roomCharts[roomKey] ?: return@Button

                        val snapshot = RoomChartSnapshot(
                            roomId = roomKey,
                            chart = currentChart.copy(
                                hygiene = mutableStateListOf(*currentChart.hygiene.toTypedArray()),
                                linen = mutableStateListOf(*currentChart.linen.toTypedArray()),
                                tasks = mutableStateListOf(*currentChart.tasks.toTypedArray()),

                                device = currentChart.device,

                                hygieneOther = currentChart.hygieneOther,
                                linenOther = currentChart.linenOther,
                                deviceOther = currentChart.deviceOther
                            )
                        )

                        session.savedRoomCharts.add(snapshot)

                        session.liveActivities.add(
                            LiveActivity(
                                workerName = worker.name,
                                roomId = roomKey,
                                message = buildString {
                                    append("Updated Room $roomKey: ")

                                    if (currentChart.hygiene.isNotEmpty())
                                        append("Hygiene ")

                                    if (currentChart.linen.isNotEmpty())
                                        append("| Linen ")

                                    if (currentChart.tasks.isNotEmpty())
                                        append("| Tasks updated")
                                }
                            )
                        )


                        session.roomCharts[roomKey] = RoomChart(roomKey)

                        selectedSnapshot = snapshot
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("Save Entry", color = Color.White)
                }

                if (viewMode && selectedRoom != null) {

                    val roomKey = selectedRoom.toString()

                    val snapshotToShow =
                        selectedSnapshot
                            ?: session.savedRoomCharts.lastOrNull { it.roomId == roomKey }

                    snapshotToShow?.let { snapshot ->

                        val c = snapshot.chart

                        fun formatList(title: String, list: List<String>): String {
                            return if (list.isEmpty()) {
                                "$title - None"
                            } else {
                                "$title - ${list.joinToString(", ")}"
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {

                            Text(
                                "ROOM ${snapshot.roomId} SUMMARY",
                                style = MaterialTheme.typography.headlineSmall,
                                color = GreenDark
                            )

                            Spacer(Modifier.height(12.dp))

                            // ================= FORMATTED SUMMARY =================
                            SummaryCard(
                                "Hygiene",
                                buildString {
                                    append(
                                        if (c.hygiene.isEmpty())
                                            "Hygiene - None"
                                        else
                                            "Hygiene - ${c.hygiene.joinToString(", ")}"
                                    )

                                    if (c.hygieneOther.isNotEmpty()) {
                                        append("\nOther: ${c.hygieneOther}")
                                    }
                                }
                            )

                            Spacer(Modifier.height(10.dp))

                            SummaryCard(
                                "Linen",
                                buildString {

                                    append(
                                        if (c.linen.isEmpty())
                                            "Linen - None"
                                        else
                                            "Linen - ${c.linen.joinToString(", ")}"
                                    )

                                    if (c.linenOther.isNotEmpty()) {
                                        append("\nOther: ${c.linenOther}")
                                    }
                                }
                            )

                            Spacer(Modifier.height(10.dp))

                            SummaryCard(
                                "Device",
                                buildString {

                                    append(
                                        "Device - ${c.device ?: "None"}"
                                    )

                                    if (c.deviceOther.isNotEmpty()) {
                                        append("\nOther: ${c.deviceOther}")
                                    }
                                }
                            )

                            Spacer(Modifier.height(10.dp))

                            SummaryCard(
                                "Meals",
                                buildString {
                                    appendLine("Intake - ${c.mealIntake} ml")
                                    appendLine("Output - ${c.mealOutputMl} ml")
                                    append("Eaten - ${c.mealPercentage}")
                                }
                            )

                            Spacer(Modifier.height(10.dp))

                            SummaryCard(
                                "Tasks",
                                if (c.tasks.isEmpty()) "Tasks - None"
                                else c.tasks.joinToString("\n") { "- $it" }
                            )

                            Spacer(Modifier.height(20.dp))

                            Button(
                                onClick = { selectedSnapshot = null },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Back")
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // SAVED
                Button(
                    onClick = {
                        showSavedEntries = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("Saved Entries")
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
fun OtherCommentBox(
    title: String,
    items: MutableList<String>
) {

    var input by remember { mutableStateOf("") }

    Column {

        Row {

            OutlinedTextField(
                value = input,
                onValueChange = {
                    input = it
                },
                label = {
                    Text(title)
                },
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(8.dp))

            Button(
                onClick = {

                    if (input.isNotBlank()) {
                        items.add(input.trim())
                        input = ""
                    }

                }
            ) {
                Text("Add")
            }
        }


        Spacer(Modifier.height(10.dp))


        items.forEach { comment ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {

                Text(
                    text = comment,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
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
@Composable
fun SummaryCard(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F3F5)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(title, color = GreenDark, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(content, color = Color.Black)
        }
    }
}

