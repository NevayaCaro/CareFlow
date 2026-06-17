package com.nevaya.careflow.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nevaya.careflow.SplashScreen
import com.nevaya.careflow.screens.*
import com.nevaya.careflow.ui.screens.*
import com.nevaya.careflow.ui.splash.OverviewSplashScreen

@Composable
fun AppNavGraph(navController: NavHostController, padding: PaddingValues, startDestination: String) {

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // SPLASH
        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("overview") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("overview") {
            OverviewSplashScreen(
                onContinue = { navController.navigate("login") },
                onSkip = { navController.navigate("login") }
            )
        }


        // LOGIN
        composable("login") {
            LoginScreen(
                onForgotPassword = { navController.navigate("forgot_password") },
                onCreateAccount = { navController.navigate("create_account") },


                onLoginClick = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },

                onQuickLogin = {
                    navController.navigate("home") {
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
                    navController.navigate("home") {
                        popUpTo("create_account") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // HOME (MAIN HUB)
        composable("home") {
            HomeScreen(
                padding = padding,
                navController = navController
            )
        }

        // CREATE GROUP
        composable("create") {
            CreateScreen(
                onDoneClick = { code ->
                    navController.navigate("creator/$code") {
                        popUpTo("create") { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // WORKER ENTRY (JOIN SCREEN FIRST)
        composable("workerCodeEntry") {
            JoinWorkerScreen(
                onJoinValid = { code ->
                    navController.navigate("worker/$code") {
                        popUpTo("workerCodeEntry") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

// WORKER ASSIGNMENT SCREEN
        composable("worker/{code}") { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code") ?: ""

            WorkerAssignmentScreen(
                sessionCode = code,
                onBack = { navController.popBackStack() }
            )
        }

        // CREATOR ASSIGNMENT (CODE ROUTE)
        composable("creator/{code}") { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code") ?: ""

            CreatorAssignmentScreen(
                sessionCode = code,
                onBack = { navController.popBackStack() }
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