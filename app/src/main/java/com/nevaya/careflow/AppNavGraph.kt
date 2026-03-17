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

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onForgotPassword = { navController.navigate("forgot_password") },
                onCreateAccount = { navController.navigate("create_account") }
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
                onSubmit = { navController.navigate("authorization") }
            )
        }

        composable("authorization") {
            AuthorizationScreen(
                onBack = { navController.popBackStack() },
                onSubmit = {
                    // TODO: Reset password screen
                }
            )
        }

        // NEW CREATE ACCOUNT SCREEN
        composable("create_account") {
            CreateAccountScreen(
                onSubmit = {
                    // Your partner will decide what happens next
                    // Example: navController.navigate("profile")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

