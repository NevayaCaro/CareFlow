package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nevaya.careflow.data.UserRepository
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadMyDataScreen(navController: NavHostController) {

    var requested by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Download My Data") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                "You can request a copy of your data associated with your CareFlow account.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    UserRepository.requestDataExport()
                    requested = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Request Data Export")
            }

            if (requested) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Your data export request has been submitted.",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}