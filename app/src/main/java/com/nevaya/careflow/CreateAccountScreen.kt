package com.nevaya.careflow.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevaya.careflow.R
import com.nevaya.careflow.data.UserProfile
import com.nevaya.careflow.data.UserSessionDataStore
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun CreateAccountScreen(
    onSubmit: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val session = remember { UserSessionDataStore(context) }
    val coroutineScope = rememberCoroutineScope()

    val savedProfilesRaw by session.savedProfiles.collectAsState(initial = "")
    val existingProfiles = remember(savedProfilesRaw) {
        UserProfile.fromListString(savedProfilesRaw)
    }

    val calendar = Calendar.getInstance()

    val datePicker = DatePickerDialog(
        context,
        { _, year, month, day ->
            dob = "${month + 1}/$day/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    fun isValidEmail(input: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 200.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.careflow_logo),
                contentDescription = "CareFlow Logo",
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name *") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Create Login Username (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = !isValidEmail(it)
                },
                label = { Text("Email *") },
                isError = emailError,
                modifier = Modifier.fillMaxWidth()
            )

            if (emailError) {
                Text(
                    text = "Invalid email format",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Create Password *") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password *") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        datePicker.show()
                    }
            ) {
                OutlinedTextField(
                    value = dob,
                    onValueChange = {},
                    label = { Text("Date of Birth (DOB) *") },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Back")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (
                            name.isNotBlank() &&
                            email.isNotBlank() &&
                            !emailError &&
                            password.isNotBlank() &&
                            confirmPassword.isNotBlank() &&
                            dob.isNotBlank() &&
                            password == confirmPassword
                        ) {

                            val finalUsername =
                                if (username.isBlank()) name.lowercase() else username

                            coroutineScope.launch {

                                val newProfile = UserProfile(
                                    name = name,
                                    username = finalUsername,
                                    email = email,
                                    password = password,
                                    dob = dob,
                                    profileImageUri = null
                                )

                                val updated = existingProfiles + newProfile

                                session.saveProfiles(
                                    UserProfile.toListString(updated)
                                )

                                session.setActiveUser(finalUsername)

                                onSubmit(finalUsername)
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Submit")
                }
            }
        }
    }
}