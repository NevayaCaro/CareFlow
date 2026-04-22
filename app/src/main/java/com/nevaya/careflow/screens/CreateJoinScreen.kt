package com.nevaya.careflow.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nevaya.careflow.ui.theme.GreenDark
import com.nevaya.careflow.ui.theme.GreenPrimary
import com.nevaya.careflow.data.SessionStore
import kotlinx.coroutines.delay

@Composable
fun CreateJoinScreen(
    modifier: Modifier = Modifier,
    onCreateClick: (String) -> Unit,
    onJoinValid: (String) -> Unit
) {

    var showJoinField by remember { mutableStateOf(false) }
    var joinCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showHelp by remember { mutableStateOf(false) }

    // controls the "pop" animation every 5 seconds
    var pulseTrigger by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pulseTrigger) 1.25f else 1f,
        animationSpec = tween(300),
        label = "helpPulse"
    )

    // every 5 seconds trigger a pulse
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            pulseTrigger = true
            delay(400)
            pulseTrigger = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            // TOP BAR
            Surface(
                color = GreenPrimary,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 16.dp)
                ) {

                    Text(
                        text = "CareFlow",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    // HELP SECTION (top right)
                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // EXPANDED HELP PANEL
                        AnimatedVisibility(
                            visible = showHelp,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        Color.White.copy(alpha = 0.95f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(10.dp)
                                    .width(220.dp)
                            ) {
                                Text(
                                    text =
                                        " Join Code: enter a 4-digit session code\n" +
                                                " Create: generates a new session and allows you to add workers and assign tasks \n" +
                                                " Share code with your team to join",
                                    color = GreenDark,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // NEED HELP BUTTON
                        Box(
                            modifier = Modifier
                                .scale(scale)
                        ) {
                            Button(
                                onClick = { showHelp = !showHelp },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GreenDark
                                ),
                                contentPadding = PaddingValues(10.dp),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Need Help",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // MAIN CARD
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .width(330.dp)
                    .align(Alignment.CenterHorizontally)
            ) {

                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Button(
                        onClick = { showJoinField = true },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Join Code", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onCreateClick("") },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Create", color = Color.White)
                    }

                    if (showJoinField) {

                        Spacer(modifier = Modifier.height(28.dp))

                        Text(
                            "Enter Code",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            repeat(4) { index ->
                                Text(
                                    text = if (index < joinCode.length) "●" else "○",
                                    color = Color.White,
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        val buttons = listOf(
                            listOf("1", "2", "3"),
                            listOf("4", "5", "6"),
                            listOf("7", "8", "9"),
                            listOf("DEL", "0", "ENT")
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            buttons.forEach { row ->
                                Row {
                                    row.forEach { label ->

                                        ElevatedButton(
                                            onClick = {
                                                when (label) {
                                                    "DEL" ->
                                                        if (joinCode.isNotEmpty())
                                                            joinCode = joinCode.dropLast(1)

                                                    "ENT" -> {
                                                        if (joinCode.length == 4) {
                                                            val session = SessionStore.getSession(joinCode)
                                                            if (session != null) onJoinValid(joinCode)
                                                            else errorMessage = "Invalid code"
                                                        } else errorMessage = "Enter 4-digit code"
                                                    }

                                                    else ->
                                                        if (joinCode.length < 4)
                                                            joinCode += label
                                                }
                                            },
                                            modifier = Modifier
                                                .padding(6.dp)
                                                .size(70.dp),
                                            shape = RoundedCornerShape(16.dp),
                                            colors = ButtonDefaults.elevatedButtonColors(
                                                containerColor = GreenDark,
                                                contentColor = Color.White
                                            )
                                        ) {
                                            Text(label)
                                        }
                                    }
                                }
                            }
                        }

                        if (errorMessage.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(errorMessage, color = Color.Red)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}