package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, padding: PaddingValues) {

    // STATE
    var notificationsEnabled by remember { mutableStateOf(true) }
    var shiftAlerts by remember { mutableStateOf(true) }
    var roomAlerts by remember { mutableStateOf(true) }
    var urgentAlerts by remember { mutableStateOf(true) }
    var messageAlerts by remember { mutableStateOf(true) }

    var darkMode by remember { mutableStateOf(false) }
    var largeText by remember { mutableStateOf(false) }
    var highContrast by remember { mutableStateOf(false) }

    var autoClockReminder by remember { mutableStateOf(true) }
    var autoRefresh by remember { mutableStateOf(true) }
    var haptics by remember { mutableStateOf(true) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(20.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {

        // HEADER
        Text(
            "Settings",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // -------------------------
        // ACCOUNT SECTION
        // -------------------------
        SectionHeader("Account")

        SettingsCard {
            SettingsRow("Edit Profile") {
                navController.navigate("edit_profile")
            }
            SettingsRow("Change Password") {
                navController.navigate("change_password")
            }
            SettingsRow("Two-Factor Authentication") {
                navController.navigate("two_factor")
            }
            SettingsRow("Logout") {
                navController.navigate("login") {
                    popUpTo("settings") { inclusive = true }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // -------------------------
        // NOTIFICATIONS
        // -------------------------
        SectionHeader("Notifications")

        SettingsCard {
            ToggleRow("Enable Notifications", notificationsEnabled) { notificationsEnabled = it }
            ToggleRow("Shift Reminders", shiftAlerts) { shiftAlerts = it }
            ToggleRow("Room Assignment Alerts", roomAlerts) { roomAlerts = it }
            ToggleRow("Urgent Patient Alerts", urgentAlerts) { urgentAlerts = it }
            ToggleRow("Message Notifications", messageAlerts) { messageAlerts = it }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // -------------------------
        // APPEARANCE
        // -------------------------
        SectionHeader("Appearance")

        SettingsCard {
            ToggleRow("Dark Mode", darkMode) { darkMode = it }
            ToggleRow("Large Text", largeText) { largeText = it }
            ToggleRow("High Contrast Mode", highContrast) { highContrast = it }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // -------------------------
        // APP BEHAVIOR
        // -------------------------
        SectionHeader("App Behavior")

        SettingsCard {
            ToggleRow("Auto Clock-In Reminder", autoClockReminder) { autoClockReminder = it }
            ToggleRow("Auto-Refresh Assignments", autoRefresh) { autoRefresh = it }
            ToggleRow("Enable Haptic Feedback", haptics) { haptics = it }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // -------------------------
        // DATA & PRIVACY
        // -------------------------
        SectionHeader("Data & Privacy")

        SettingsCard {
            SettingsRow("Download My Data") {
                navController.navigate("download_data")
            }
            SettingsRow("Clear App Cache") {
                navController.navigate("clear_cache")
            }
            SettingsRow("Privacy Policy") { }
            SettingsRow("Terms of Service") { }
            SettingsRow("Delete My Account") {
                navController.navigate("delete_account")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // -------------------------
        // SUPPORT
        // -------------------------
        SectionHeader("Support")

        SettingsCard {
            SettingsRow("Contact Support") {
                navController.navigate("contact_support")
            }
            SettingsRow("Report an Issue") {
                navController.navigate("report_issue")
            }
            SettingsRow("FAQ") { }
            SettingsRow("About CareFlow") { }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // -------------------------
        // APP INFO
        // -------------------------
        SectionHeader("App Info")

        SettingsCard {
            SettingsRow("Version 1.0") { }
            SettingsRow("Check for Updates") { }
            SettingsRow("Release Notes") { }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.secondary
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
fun SettingsRow(label: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            label,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ToggleRow(label: String, state: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurface)
        Switch(
            checked = state,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.secondary,
                checkedTrackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            )
        )
    }
}