package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(
    onEditProfile: () -> Unit = {},
    onViewSchedule: () -> Unit = {},
    onViewAssignments: () -> Unit = {}
) {

    // 🔥 CLOCK STATE
    var isClockedIn by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            // 🔹 HEADER (Profile Info)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text("John Doe", fontSize = 20.sp)
                        Text("@johndoe", fontSize = 14.sp)
                    }
                }

                IconButton(onClick = onEditProfile) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 SHIFT STATUS CARD (NOW DYNAMIC)
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {
                        Text("Status", fontSize = 14.sp)
                        Text(
                            if (isClockedIn) "On Duty" else "Off Duty",
                            fontSize = 18.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            if (isClockedIn) "Clocked In At" else "Last Clock Out",
                            fontSize = 14.sp
                        )
                        Text("7:00 AM") // later → replace with real time
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // 🔹 PERSONAL INFO
            Text("Personal Information", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(12.dp))

            ProfileInfoRow(label = "Name", value = "John Doe")
            ProfileInfoRow(label = "Date of Birth", value = "01/01/2000")
            ProfileInfoRow(label = "Username", value = "@johndoe")

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // 🔹 WORK INFO
            Text("Work Information", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text("Next Shift", fontSize = 16.sp)
                    Text("March 18, 2026 - 7:00 AM to 3:00 PM")

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Room Assignment", fontSize = 16.sp)
                    Text("Room 204 – J.S.")
                    Text("• Fall Risk")
                    Text("• Assisted Mobility")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onViewSchedule,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View Full Schedule")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 🔹 ACTION BUTTONS (FULLY FUNCTIONAL)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        // ✅ CLOCK BUTTON (TOGGLE)
                        OutlinedButton(
                            onClick = { isClockedIn = !isClockedIn }
                        ) {
                            Text(if (isClockedIn) "Clock Out" else "Clock In")
                        }

                        // ✅ ASSIGNMENTS BUTTON
                        OutlinedButton(
                            onClick = onViewAssignments
                        ) {
                            Text("Assignments")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(value)
    }
}