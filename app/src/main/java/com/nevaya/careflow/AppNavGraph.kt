package com.nevaya.careflow.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nevaya.careflow.SplashScreen
import com.nevaya.careflow.ui.screens.*

@Composable
fun AppNavGraph(navController: NavHostController, padding: PaddingValues) {

    NavHost(
        navController = navController,
        startDestination = "home"
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
                onLoginClick = { username ->
                    navController.navigate("profile") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onQuickLogin = { username ->   // ⭐ REQUIRED
                    navController.navigate("profile") {
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
                    navController.navigate("profile") {
                        popUpTo("create_account") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // PROFILE
        composable("profile") {
            ProfileScreen(
                onEditProfile = { navController.navigate("edit_profile") },
                onViewSchedule = { navController.navigate("schedule") },
                onViewAssignments = { navController.navigate("assignments") }
            )
        }

        // HOME
        composable("home") {
            HomeScreen(padding)
        }

        // SCHEDULE
        composable("schedule") {
            ScheduleScreen(padding)
        }

        // ASSIGNMENTS
        composable("assignments") {
            RoomAssignmentsScreen(padding)
        }

        // MESSAGES
        composable("messages") {
            MessagesScreen(navController, padding)
        }

        // CHAT SCREEN (NEW)
        composable("chat/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            ChatScreen(navController, id)
        }

        // SETTINGS
        composable("settings") {
            SettingsScreen(navController, padding)
        }

        // -----------------------------
        // SETTINGS SUB-SCREENS
        // -----------------------------

        // ACCOUNT
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

