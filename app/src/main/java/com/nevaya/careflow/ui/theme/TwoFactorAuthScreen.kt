package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nevaya.careflow.data.UserRepository
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoFactorAuthScreen(navController: NavHostController) {

    var enabled by remember { mutableStateOf(UserRepository.twoFactorEnabled) }
    var saved by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Two-Factor Authentication") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(50.dp)
                .fillMaxSize()
        ) {

            Text(
                "Add an extra layer of security to your account by enabling two-factor authentication.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enable Two-Factor Authentication")
                Switch(
                    checked = enabled,
                    onCheckedChange = {
                        enabled = it
                        saved = false
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // ⭐ FIXED — use the correct function
                    UserRepository.updateTwoFactor(enabled)
                    saved = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }

            if (saved) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Two-factor settings updated.",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}