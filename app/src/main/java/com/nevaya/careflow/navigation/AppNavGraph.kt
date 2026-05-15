package com.nevaya.careflow.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nevaya.careflow.SplashScreen
import com.nevaya.careflow.screens.CreateJoinScreen
import com.nevaya.careflow.screens.CreateScreen
import com.nevaya.careflow.screens.CreatorAssignmentScreen
import com.nevaya.careflow.screens.WorkerAssignmentScreen
import com.nevaya.careflow.ui.screens.*

@Composable
fun AppNavGraph(navController: NavHostController, padding: PaddingValues) {

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        // SPLASH
        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // LOGIN
        composable("login") {
            LoginScreen(
                onForgotPassword = { navController.navigate("forgot_password") },
                onCreateAccount = { navController.navigate("create_account") },
                onLoginClick = {
                    navController.navigate("createJoin") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onQuickLogin = {
                    navController.navigate("createJoin") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // FORGOT PASSWORD
        composable("forgot_password") {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
                onSubmit = { navController.navigate("authorization") }
            )
        }

        // AUTHORIZATION
        composable("authorization") {
            AuthorizationScreen(
                onBack = { navController.popBackStack() },
                onSubmit = { }
            )
        }

        // CREATE ACCOUNT
        composable("create_account") {
            CreateAccountScreen(
                onSubmit = {
                    navController.navigate("createJoin") {
                        popUpTo("create_account") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ⭐ CREATE / JOIN SCREEN (ADDED)
        composable("createJoin") {
            CreateJoinScreen(
                onCreateClick = {
                    navController.navigate("create")
                },
                onJoinValid = {
                    navController.navigate("worker")
                }
            )
        }

        // ⭐ CREATE SCREEN (ADDED)
        composable("create") {
            CreateScreen(
                onDoneClick = { code ->
                    navController.navigate("home") {
                        popUpTo("create") { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // PROFILE
        composable("profile") {
            ProfileScreen(
                navController = navController,
                onEditProfile = { navController.navigate("edit_profile") },
                onViewPatients = { navController.navigate("patients") },
                onViewAssignments = { navController.navigate("assignments") }
            )
        }

        // HOME
        composable("home") {
            HomeScreen(
                padding = padding,
                navController = navController
            )
        }

        // PATIENTS
        composable("patients") {
            PatientsScreen(navController)
        }

        // PATIENT CARD
        composable("patient_card/{roomNumber}") { backStackEntry ->
            val roomNumber = backStackEntry.arguments?.getString("roomNumber")?.toInt() ?: 0
            PatientCardScreen(navController, roomNumber)
        }

        // ASSIGNMENTS
        composable("assignments") {
            RoomAssignmentsScreen(
                padding = padding,
                navController = navController
            )
        }


        // MESSAGES
        composable("messages") {
            MessagesScreen(navController, padding)
        }

        // CHAT
        composable("chat/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            ChatScreen(navController, id)
        }

        // SETTINGS
        composable("settings") {
            SettingsScreen(navController, padding)
        }

        // SETTINGS SUB-SCREENS
        composable("edit_profile") { EditProfileScreen(navController) }
        composable("change_password") { ChangePasswordScreen(navController) }
        composable("two_factor") { TwoFactorAuthScreen(navController) }

        // DATA & PRIVACY
        composable("download_data") { DownloadMyDataScreen(navController) }
        composable("clear_cache") { ClearCacheScreen(navController) }
        composable("delete_account") { DeleteAccountScreen(navController) }

        // SUPPORT
        composable("contact_support") { ContactSupportScreen(navController) }
        composable("report_issue") { ReportIssueScreen(navController) }
    }
}
