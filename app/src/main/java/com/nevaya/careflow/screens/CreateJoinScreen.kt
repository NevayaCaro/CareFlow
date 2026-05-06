package com.nevaya.careflow.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nevaya.careflow.ui.theme.GreenDark
import com.nevaya.careflow.ui.theme.GreenPrimary
import com.nevaya.careflow.data.SessionStore
import kotlinx.coroutines.delay

@Composable
fun CreateJoinScreen(
    modifier: Modifier = Modifier,
    onCreateClick: (String) -> Unit,
    onJoinValid: (String) -> Unit,
    onCreatorValid: (String) -> Unit
) {

    var showJoinField by remember { mutableStateOf(false) }
    var joinCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showHelp by remember { mutableStateOf(false) }

    var pulseTrigger by remember { mutableStateOf(false) }

    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (pulseTrigger) 1.25f else 1f,
        animationSpec = androidx.compose.animation.core.tween(300),
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
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            //  TOP BAR (MATCHED STYLE)
            Surface(
                color = GreenPrimary,
                shadowElevation = 6.dp,
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp, horizontal = 16.dp)
                ) {

                    Text(
                        text = "CareFlow",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    // HELP BUTTON
                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        AnimatedVisibility(
                            visible = showHelp,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        Color.White,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(12.dp)
                                    .width(220.dp)
                            ) {
                                Text(
                                    text =
                                        "Join Code: enter 4-digit session\n" +
                                                "Create: starts new shift session\n" +
                                                "Share code with team",
                                    color = GreenDark,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(modifier = Modifier.scale(scale)) {
                            IconButton(
                                onClick = { showHelp = !showHelp }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            //  MAIN CARD (MATCHED STYLE)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp),
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
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Join Code", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { onCreateClick("") },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Create", color = Color.White)
                    }

                    if (showJoinField) {

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            "Enter Code",
                            color = GreenDark,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            repeat(4) { index ->
                                Text(
                                    text = if (index < joinCode.length) "●" else "○",
                                    color = GreenPrimary,
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

                                                            // check worker code first
                                                            val workerSession = SessionStore.getSession(joinCode)

                                                            if (workerSession != null) {
                                                                onJoinValid(joinCode) // worker flow
                                                            } else {

                                                                // check creator code
                                                                val creatorSession = SessionStore.getSessionByCreatorCode(joinCode)

                                                                if (creatorSession != null) {
                                                                    onJoinValid(creatorSession.code) // pass real session code
                                                                } else {
                                                                    errorMessage = "Invalid code"
                                                                }
                                                            }

                                                        } else {
                                                            errorMessage = "Enter 4-digit code"
                                                        }
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