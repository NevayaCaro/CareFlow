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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ProfileScreen(
    onEditProfile: () -> Unit = {},
    onViewSchedule: () -> Unit = {},
    onViewAssignments: () -> Unit = {}
) {

    var isClockedIn by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {

            // HEADER
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
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.secondary   // DARK GREEN
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            "John Doe",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            "@johndoe",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                    }
                }

                IconButton(onClick = onEditProfile) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = MaterialTheme.colorScheme.secondary   // DARK GREEN
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // STATUS CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {
                        Text(
                            "Status",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            if (isClockedIn) "On Duty" else "Off Duty",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.secondary   // DARK GREEN
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            if (isClockedIn) "Clocked In At" else "Last Clock Out",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "7:00 AM",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)   // DARK GREEN
            )

            // PERSONAL INFO
            Text(
                "Personal Information",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileInfoRow(label = "Name", value = "John Doe")
            ProfileInfoRow(label = "Date of Birth", value = "01/01/2000")
            ProfileInfoRow(label = "Username", value = "@johndoe")

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)   // DARK GREEN
            )

            // WORK INFO
            Text(
                "Work Information",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        "Next Shift",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "March 18, 2026 - 7:00 AM to 3:00 PM",
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "Room Assignment",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text("Room 204 – J.S.", color = MaterialTheme.colorScheme.onSurface)
                    Text("• Fall Risk", color = MaterialTheme.colorScheme.onSurface)
                    Text("• Assisted Mobility", color = MaterialTheme.colorScheme.onSurface)

                    Spacer(modifier = Modifier.height(16.dp))

                    // FULL SCHEDULE BUTTON
                    Button(
                        onClick = onViewSchedule,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,   // DARK GREEN
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Text("View Full Schedule")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // ACTION BUTTONS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        OutlinedButton(
                            onClick = { isClockedIn = !isClockedIn },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.secondary   // DARK GREEN
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = SolidColor(MaterialTheme.colorScheme.secondary)
                            )
                        ) {
                            Text(if (isClockedIn) "Clock Out" else "Clock In")
                        }

                        OutlinedButton(
                            onClick = onViewAssignments,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.secondary   // DARK GREEN
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = SolidColor(MaterialTheme.colorScheme.secondary)
                            )
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
        Text(
            label,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            value,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

