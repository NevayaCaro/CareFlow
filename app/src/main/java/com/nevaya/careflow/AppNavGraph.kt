package com.nevaya.careflow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nevaya.careflow.SplashScreen
import com.nevaya.careflow.ui.screens.LoginScreen
import com.nevaya.careflow.ui.screens.ForgotPasswordScreen
import com.nevaya.careflow.ui.screens.AuthorizationScreen
import com.nevaya.careflow.ui.screens.CreateAccountScreen
import com.nevaya.careflow.ui.screens.ProfileScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "profile"
    ) {

        // 🔹 SPLASH
        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // 🔹 LOGIN
        composable("login") {
            LoginScreen(
                onForgotPassword = { navController.navigate("forgot_password") },
                onCreateAccount = { navController.navigate("create_account") }
            )
        }

        // 🔹 FORGOT PASSWORD
        composable("forgot_password") {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
                onSubmit = { navController.navigate("authorization") }
            )
        }

        // 🔹 AUTHORIZATION
        composable("authorization") {
            AuthorizationScreen(
                onBack = { navController.popBackStack() },
                onSubmit = {
                    // Future: navigate to reset password screen
                }
            )
        }

        // 🔹 CREATE ACCOUNT
        composable("create_account") {
            CreateAccountScreen(
                onSubmit = {
                    navController.navigate("profile") {
                        popUpTo("create_account") { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // 🔹 PROFILE SCREEN (NEW)
        composable("profile") {
            ProfileScreen(
                onEditProfile = {

                },
                onViewSchedule = {
                    navController.navigate("schedule")
                },
                onViewAssignments = {
                    navController.navigate("assignments")
                }
            )
        }
    }
}

