package com.nevaya.careflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.nevaya.careflow.ui.theme.CareFlowTheme



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithFloatingMenu(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CareFlow") }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            content(PaddingValues())

            // Floating menu layer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(10f)
            ) {

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart)
                ) {

                    // GREEN MENU BUTTON
                    IconButton(
                        onClick = { menuExpanded = true },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }

                    // GREEN DROPDOWN MENU
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                    ) {

                        FloatingMenuItem("Home", Icons.Default.Home) {
                            navController.navigate("home")
                            menuExpanded = false
                        }
                        FloatingMenuItem("Profile", Icons.Default.Person) {
                            navController.navigate("profile")
                            menuExpanded = false
                        }
                        FloatingMenuItem("Settings", Icons.Default.Settings) {
                            navController.navigate("settings")
                            menuExpanded = false
                        }
                        FloatingMenuItem("Room Assignments", Icons.AutoMirrored.Filled.List) {
                            navController.navigate("assignments")
                            menuExpanded = false
                        }
                        FloatingMenuItem("Messages", Icons.Default.Email) {
                            navController.navigate("messages")
                            menuExpanded = false
                        }
                        FloatingMenuItem("Shift Schedule", Icons.Default.CalendarToday) {
                            navController.navigate("schedule")
                            menuExpanded = false
                        }
                        FloatingMenuItem("Logout", Icons.AutoMirrored.Filled.ExitToApp) {
                            navController.navigate("login")
                            menuExpanded = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FloatingMenuItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(
                title,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        leadingIcon = {
            Icon(
                icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        onClick = onClick
    )
}

