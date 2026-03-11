package com.nevaya.careflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CreateJoinScreen(modifier: Modifier = Modifier) {
// Creates a screen function with an optional modifier

    var showJoinField by remember { mutableStateOf(false) }
    var joinCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFBFC9D1)) // screen background color
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "🧡",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            //logo centered at top of screen

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "back",
                    color = Color.White
                )
            }
            //back button under logo on the left

            Spacer(modifier = Modifier.weight(1f))

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .width(320.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                //white box centered in middle of screen

                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Button(
                        onClick = { showJoinField = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD1D7DB)
                        ),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                    ) {
                        Text(
                            text = "Join Code",
                            color = Color(0xFF25343F)
                        )
                    }
                    //rounded join code button

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { /* TODO: Create Shift */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD1D7DB)
                        ),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                    ) {
                        Text(
                            text = "Create",
                            color = Color(0xFF25343F)
                        )
                    }
                    //second rounded button


                    if (showJoinField) {

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                            value = joinCode,
                            onValueChange = {
                                if (it.all { char -> char.isDigit() } && it.length <= 6) {
                                    joinCode = it
                                    errorMessage = ""
                                } else {
                                    errorMessage = "Numbers only (4-6 digits)"
                                }
                            },
                            label = { Text("Enter Code") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            singleLine = true
                        )

                        if (errorMessage.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = errorMessage,
                                color = Color.Red
                            )
                        }
                    }
                    //join code input appears when button pressed
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}