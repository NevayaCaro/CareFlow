package com.nevaya.careflow.ui.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun OverviewSplashScreen(
    onContinue: () -> Unit,
    onSkip: () -> Unit
) {
    var showTitle by remember { mutableStateOf(false) }
    var showSetup by remember { mutableStateOf(false) }
    var showInside by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }

    // Staggered animation sequence
    LaunchedEffect(true) {
        delay(200)
        showTitle = true
        delay(400)
        showSetup = true
        delay(400)
        showInside = true
        delay(400)
        showButtons = true
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {

            // Skip Button (top-right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                AnimatedVisibility(showButtons) {
                    Text(
                        text = "Skip",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { onSkip() }
                    )
                }
            }

            // Title
            AnimatedVisibility(
                visible = showTitle,
                enter = fadeIn(animationSpec = tween(600)) +
                        slideInVertically(initialOffsetY = { 40 }),
                exit = fadeOut()
            ) {
                Text(
                    text = "How CareFlow Works",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Session Setup Section
            AnimatedVisibility(
                visible = showSetup,
                enter = fadeIn(animationSpec = tween(600)) +
                        slideInVertically(initialOffsetY = { 40 }),
                exit = fadeOut()
            ) {
                Column {
                    Text(
                        text = "Session Setup",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    BulletPoint(Icons.Default.Group, "Create a group and generate a session code")
                    BulletPoint(Icons.Default.Key, "Workers join using that code to enter the shift")
                    BulletPoint(Icons.Default.Refresh, "Save and reuse codes to continue sessions")
                    BulletPoint(Icons.Default.Login, "Re-enter a session anytime using the creator code")
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Inside Session Section
            AnimatedVisibility(
                visible = showInside,
                enter = fadeIn(animationSpec = tween(600)) +
                        slideInVertically(initialOffsetY = { 40 }),
                exit = fadeOut()
            ) {
                Column {
                    Text(
                        text = "Inside a Session",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    BulletPoint(Icons.Default.MedicalServices, "Creators manage patient info, rooms, and discharges")
                    BulletPoint(Icons.Default.Assignment, "Creators assign workers to rooms, meals, and shower tasks")
                    BulletPoint(Icons.Default.Update, "Workers update tasks and room details in real time")
                    BulletPoint(Icons.Default.Visibility, "Creators monitor live updates as workers enter data")
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Continue Button
            AnimatedVisibility(
                visible = showButtons,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Button(
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Continue")
                }
            }
        }
    }
}

@Composable
fun BulletPoint(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
