package com.nevaya.careflow.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nevaya.careflow.data.SessionStore
import kotlinx.coroutines.delay

@Composable
fun JoinWorkerScreen(
    onJoinValid: (String) -> Unit,
    onBack: () -> Unit
) {

    var showJoinField by remember { mutableStateOf(false) }
    var joinCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showHelp by remember { mutableStateOf(false) }

    var pulseTrigger by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pulseTrigger) 1.25f else 1f,
        animationSpec = tween(300),
        label = "helpPulse"
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            pulseTrigger = true
            delay(400)
            pulseTrigger = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            // TOP BAR
            Surface(
                color = Color(0xFF2E7D32),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    Text(
                        text = "CareFlow",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    IconButton(
                        onClick = { showHelp = !showHelp },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .width(330.dp)
                    .align(Alignment.CenterHorizontally)
            ) {

                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Button(
                        onClick = { showJoinField = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Join Code", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onBack() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Back", color = Color.White)
                    }

                    if (showJoinField) {

                        Spacer(modifier = Modifier.height(20.dp))

                        Text("Enter Code", fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(10.dp))

                        Row {
                            repeat(4) { i ->
                                Text(
                                    text = if (i < joinCode.length) "●" else "○",
                                    modifier = Modifier.padding(6.dp)
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

                        Column {
                            buttons.forEach { row ->
                                Row {
                                    row.forEach { label ->

                                        Button(
                                            onClick = {
                                                when (label) {
                                                    "DEL" -> {
                                                        if (joinCode.isNotEmpty()) {
                                                            joinCode = joinCode.dropLast(1)
                                                        }
                                                    }

                                                    "ENT" -> {
                                                        if (joinCode.length == 4) {
                                                            val session = SessionStore.getSession(joinCode)

                                                            if (session != null) {
                                                                onJoinValid(joinCode)
                                                            } else {
                                                                errorMessage = "Invalid code"
                                                            }
                                                        } else {
                                                            errorMessage = "Enter 4-digit code"
                                                        }
                                                    }

                                                    else -> {
                                                        if (joinCode.length < 4) {
                                                            joinCode += label
                                                        }
                                                    }
                                                }
                                            },
                                            modifier = Modifier
                                                .padding(6.dp)
                                                .size(60.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
                                        ) {
                                            Text(label, color = Color.White)
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
        }
    }
}