package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nevaya.careflow.data.UserProfile
import com.nevaya.careflow.data.UserSessionDataStore
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(navController: NavHostController) {

    val context = LocalContext.current
    val session = remember { UserSessionDataStore(context) }
    val coroutineScope = rememberCoroutineScope()

    val activeUser by session.activeUser.collectAsState(initial = "")
    val savedProfilesRaw by session.savedProfiles.collectAsState(initial = "")
    val profiles = remember(savedProfilesRaw) { UserProfile.fromListString(savedProfilesRaw) }

    val currentProfile = profiles.find { it.username == activeUser }

    var name by remember { mutableStateOf(currentProfile?.name ?: "") }
    var username by remember { mutableStateOf(currentProfile?.username ?: "") }
    var email by remember { mutableStateOf(currentProfile?.email ?: "") }
    var password by remember { mutableStateOf(currentProfile?.password ?: "") }
    var dob by remember { mutableStateOf(currentProfile?.dob ?: "") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Edit Profile",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = dob,
                onValueChange = { dob = it },
                label = { Text("Date of Birth") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Back")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            val updatedProfile = UserProfile(
                                name = name,
                                username = username,
                                email = email,
                                password = password,
                                dob = dob,
                                profileImageUri = currentProfile?.profileImageUri
                            )

                            val updatedList = profiles.map {
                                if (it.username == activeUser) updatedProfile else it
                            }

                            session.saveProfiles(UserProfile.toListString(updatedList))
                            session.setActiveUser(username)

                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }
            }
        }
    }
}