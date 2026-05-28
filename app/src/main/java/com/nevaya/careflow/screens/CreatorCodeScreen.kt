package com.nevaya.careflow.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.nevaya.careflow.data.SessionStore
import com.nevaya.careflow.ui.theme.GreenDark
import com.nevaya.careflow.ui.theme.GreenPrimary

@Composable
fun CreatorCodeScreen(
    onBack: () -> Unit,
    onJoin: (String) -> Unit
) {

    var creatorCode by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // TOP BAR
        Surface(
            color = GreenPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                Text(
                    text = "Creator Access",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Enter Creator Code",
                style = MaterialTheme.typography.headlineSmall,
                color = GreenDark
            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = creatorCode,
                onValueChange = {
                    if (it.length <= 4 && it.all(Char::isDigit)) {
                        creatorCode = it
                    }
                },
                label = {
                    Text("4 Digit Code")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {

                    val session =
                        SessionStore.getSessionByCreatorCode(creatorCode)

                    if (session != null) {
                        onJoin(session.code)
                    } else {
                        errorMessage = "Invalid creator code"
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenDark
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Access Shift")
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = onBack) {
                Text("Back")
            }
        }
    }
}