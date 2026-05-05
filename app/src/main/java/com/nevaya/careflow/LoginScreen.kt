package com.nevaya.careflow.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevaya.careflow.R
import com.nevaya.careflow.data.UserProfile
import com.nevaya.careflow.data.UserSessionDataStore
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onForgotPassword: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
    onLoginClick: (String) -> Unit = {},   // username login
    onQuickLogin: (String) -> Unit = {}    // ⭐ added for tap-to-login
) {
    var emailOrUsername by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val session = remember { UserSessionDataStore(context) }
    val coroutineScope = rememberCoroutineScope()

    val savedProfilesRaw by session.savedProfiles.collectAsState(initial = null)
    val profiles = remember(savedProfilesRaw) {
        UserProfile.fromListString(savedProfilesRaw)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.careflow_logo),
                contentDescription = "CareFlow Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "CareFlow",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = emailOrUsername,
                onValueChange = {
                    emailOrUsername = it
                    loginError = false
                },
                label = { Text("Email or Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    loginError = false
                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            if (loginError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Invalid login credentials",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Forgot Password?",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onForgotPassword() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val match = profiles.firstOrNull {
                        (it.email.equals(emailOrUsername, ignoreCase = true) ||
                                it.username.equals(emailOrUsername, ignoreCase = true)) &&
                                it.password == password
                    }

                    if (match != null) {
                        coroutineScope.launch {
                            session.setActiveUser(match.username)
                        }
                        onLoginClick(match.username)
                    } else {
                        loginError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Login")
            }

            Button(
                onClick = onCreateAccount,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Create Account")
            }

            // ⭐ TAP-TO-LOGIN SECTION
            if (profiles.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Tap your profile to sign in",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                profiles.forEach { profile ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                coroutineScope.launch {
                                    session.setActiveUser(profile.username)
                                }
                                onQuickLogin(profile.username)   // ⭐ now works
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.careflow_logo),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(profile.name, fontWeight = FontWeight.Bold)
                            Text(profile.username, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}