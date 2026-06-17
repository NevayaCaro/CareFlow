package com.nevaya.careflow.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nevaya.careflow.SplashScreen
import com.nevaya.careflow.screens.*
import com.nevaya.careflow.ui.onboarding.OnboardingPagerScreen
import com.nevaya.careflow.SplashScreen
import com.nevaya.careflow.screens.JoinWorkerScreen
import com.nevaya.careflow.screens.WorkerAssignmentScreen
import com.nevaya.careflow.screens.CreatorAssignmentScreen
import com.nevaya.careflow.screens.CreatorCodeScreen
import com.nevaya.careflow.ui.onboarding.OnboardingPagerScreen

// UI screens (adjust if your package differs)
import com.nevaya.careflow.ui.screens.LoginScreen
import com.nevaya.careflow.ui.screens.ForgotPasswordScreen
import com.nevaya.careflow.ui.screens.AuthorizationScreen
import com.nevaya.careflow.ui.screens.CreateAccountScreen
import com.nevaya.careflow.ui.screens.HomeScreen
import com.nevaya.careflow.ui.screens.ProfileScreen
import com.nevaya.careflow.ui.screens.RoomAssignmentsScreen
import com.nevaya.careflow.ui.screens.MessagesScreen
import com.nevaya.careflow.ui.screens.ChatScreen
import com.nevaya.careflow.ui.screens.SettingsScreen
import com.nevaya.careflow.ui.screens.EditProfileScreen
import com.nevaya.careflow.ui.screens.ChangePasswordScreen
import com.nevaya.careflow.ui.screens.TwoFactorAuthScreen
import com.nevaya.careflow.ui.screens.DownloadMyDataScreen
import com.nevaya.careflow.ui.screens.ClearCacheScreen
import com.nevaya.careflow.ui.screens.DeleteAccountScreen
import com.nevaya.careflow.ui.screens.ContactSupportScreen
import com.nevaya.careflow.ui.screens.ReportIssueScreen
import com.nevaya.careflow.screens.JoinWorkerScreen
import com.nevaya.careflow.screens.WorkerAssignmentScreen
import com.nevaya.careflow.screens.CreatorAssignmentScreen
import com.nevaya.careflow.screens.CreatorCodeScreen

import com.nevaya.careflow.ui.screens.*

@Composable
fun AppNavGraph(
    navController: NavHostController,
    padding: PaddingValues,
    startDestination: String
) {

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        // SPLASH
        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // ONBOARDING
        composable("onboarding") {
            OnboardingPagerScreen(navController)
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

        // AUTH
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

        // HOME
        composable("home") {
            HomeScreen(
                padding = padding,
                navController = navController
            )
        }

        // CREATE SESSION
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


        composable("creator_code") {
            CreatorCodeScreen(
                onJoin = { code ->
                    navController.navigate("creator/$code") {
                        popUpTo("creator_code") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // WORKER CODE ENTRY
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

        // WORKER ASSIGNMENT
        composable("worker/{code}") { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code") ?: ""

            WorkerAssignmentScreen(
                sessionCode = code,
                onBack = { navController.popBackStack() }
            )
        }

        // CREATOR ASSIGNMENT
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

        composable("edit_profile") { EditProfileScreen(navController) }
        composable("change_password") { ChangePasswordScreen(navController) }
        composable("two_factor") { TwoFactorAuthScreen(navController) }

        composable("download_data") { DownloadMyDataScreen(navController) }
        composable("clear_cache") { ClearCacheScreen(navController) }
        composable("delete_account") { DeleteAccountScreen(navController) }

        composable("contact_support") { ContactSupportScreen(navController) }
        composable("report_issue") { ReportIssueScreen(navController) }
    }
}