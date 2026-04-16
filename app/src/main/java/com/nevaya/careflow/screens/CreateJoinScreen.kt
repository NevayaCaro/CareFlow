package com.nevaya.careflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nevaya.careflow.ui.theme.GreenDark
import com.nevaya.careflow.ui.theme.GreenPrimary
import com.nevaya.careflow.data.SessionStore

@Composable
fun CreateJoinScreen(
    modifier: Modifier = Modifier,
    onCreateClick: (String) -> Unit,
    onJoinValid: (String) -> Unit
) {

    var showJoinField by remember { mutableStateOf(false) }
    var joinCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "CareFlow",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.weight(1f))

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                modifier = Modifier
                    .width(320.dp)
                    .align(Alignment.CenterHorizontally)
            ) {

                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // JOIN BUTTON
                    Button(
                        onClick = { showJoinField = true },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                    ) {
                        Text("Join Code")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // CREATE BUTTON
                    Button(
                        onClick = {
                            onCreateClick("")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                    ) {
                        Text("Create")
                    }

                    // JOIN INPUT
                    if (showJoinField) {

                        Spacer(modifier = Modifier.height(24.dp))

                        Text("Enter Code")

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            repeat(4) { index ->
                                Text(
                                    text = if (index < joinCode.length) "●" else "○",
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

                                                            val session =
                                                                SessionStore.getSession(joinCode)

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
                                                .size(70.dp)
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

