package com.nevaya.careflow.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateJoinScreen(modifier: Modifier = Modifier) {
// Creates a screen function with an optional modifier
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
         //makes items vertical, added 24dp space, makes
        // column fill the screen, and centers item horizontal/vertical
        Text(
            text = "CareFlow",
            style = MaterialTheme.typography.headlineLarge
        )
//displays the text careflow on the screen large
        Spacer(modifier = Modifier.height(40.dp))
//adds 40dp space between these two
        Button(
            onClick = { /* TODO: Navigate to Create Shift */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Shift")
        }
//creates filled button does not work to
// click on fully yet, stretched text, and
// shows text "Create Shift"
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { /* TODO: Navigate to Join Shift */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Join Shift")
        }//made another bottom does same thing as above
    }
}