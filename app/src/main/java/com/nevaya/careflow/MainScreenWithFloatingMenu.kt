package com.nevaya.careflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.nevaya.careflow.data.UserSessionDataStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithFloatingMenu(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val context = LocalContext.current
    val session = remember { UserSessionDataStore(context) }
    val coroutineScope = rememberCoroutineScope()

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

            content(padding)

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

                    IconButton(
                        onClick = { menuExpanded = true },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }

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
                            coroutineScope.launch {
                                session.clearActiveUser()
                            }
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
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
                tint = MaterialTheme.colorScheme.secondary
            )
        },
        onClick = onClick
    )
}