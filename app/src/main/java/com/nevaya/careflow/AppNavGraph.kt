package com.nevaya.careflow.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nevaya.careflow.SplashScreen
import com.nevaya.careflow.ui.screens.*
import com.nevaya.careflow.ui.theme.CareFlowTheme

@Composable
fun AppNavGraph(navController: NavHostController, padding: PaddingValues) {

    NavHost(
        navController = navController,
        startDestination = "settings"
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
                onCreateAccount = { navController.navigate("create_account") }
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
                onEditProfile = {},
                onViewSchedule = { navController.navigate("schedule") },
                onViewAssignments = { navController.navigate("assignments") }
            )
        }

        // HOME (updated)
        composable("home") {
            HomeScreen(padding)
        }

        // SCHEDULE (updated)
        composable("schedule") {
            ScheduleScreen(padding)
        }

        // ASSIGNMENTS (updated)
        composable("assignments") {
            RoomAssignmentsScreen(padding)
        }

        // MESSAGES (updated)
        composable("messages") {
            MessagesScreen(padding)
        }

        // SETTINGS (updated)
        composable("settings") {
            SettingsScreen(padding)
        }
    }
}

