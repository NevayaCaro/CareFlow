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
import com.nevaya.careflow.ui.theme.GreenDark
import com.nevaya.careflow.ui.theme.GreenPrimary


@Composable
fun CreateJoinScreen(
    modifier: Modifier = Modifier,
    onCreateClick: () -> Unit
) {
// Main composable function for the Join/Create screen, accepts an optional Modifier

    var showJoinField by remember { mutableStateOf(false) }
    var joinCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    //tracks if valid input is added, input field should be visible, and optional modifier

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // uses AppBackground

    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "CareFlow",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            //logo centered at top of screen

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) { //Row for the back button and text, aligned vertically center

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = GreenDark                             // back button also PurpleDark
                ) //back button
                Text(
                    text = "back",
                    color = MaterialTheme.colorScheme.onSecondary
                )//back button text
            }
            //placed back button under logo on the left

            Spacer(modifier = Modifier.weight(1f))

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = GreenPrimary,           // Purple card
                    contentColor = MaterialTheme.colorScheme.onPrimary // white text/icons inside
                ),
                modifier = Modifier
                    .width(320.dp)
                    .align(Alignment.CenterHorizontally)
            ){
                //white box centered in middle of screen

                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {//made column inside that white box

                    Button(
                        onClick = { showJoinField = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenDark,                // PurpleDark button
                            contentColor = MaterialTheme.colorScheme.onPrimary // white text
                        ),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                    ) {//button join code
                        Text(
                            text = "Join Code",
                        )
                    }
                    //rounded join code button

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onCreateClick() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenDark,                // PurpleDark button
                            contentColor = MaterialTheme.colorScheme.onPrimary // white text
                        ),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                    ) {
                        Text(text = "Create")
                    }
                    //second rounded button


                    if (showJoinField) {
//show join code input
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
                            },//update join code if only input is digits
                            label = { Text("Enter Code") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),//gives numeric keyboard
                            singleLine = true
                        )//limits input into a single line

                        if (errorMessage.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = errorMessage,
                                color = Color.Red
                            )//show error message if a number isn't entered
                        }
                    }
                    //join code input appears when button pressed
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }//moves card to center
    }
}


