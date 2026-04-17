package com.nevaya.careflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nevaya.careflow.navigation.AppNavGraph
import com.nevaya.careflow.ui.components.MainScreenWithFloatingMenu
import com.nevaya.careflow.ui.theme.CareFlowTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            CareFlowTheme {

                val navController = rememberNavController()
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreen {
                        showSplash = false
                    }
                } else {

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val authScreens = listOf(
                        "login",
                        "create_account",
                        "forgot_password",
                        "authorization"
                    )

                    // ⭐ FIX: Treat null route as an auth screen
                    val isAuthScreen = currentRoute == null || currentRoute in authScreens

                    if (isAuthScreen) {
                        AppNavGraph(navController, PaddingValues(0.dp))
                    } else {
                        MainScreenWithFloatingMenu(navController) { padding ->
                            AppNavGraph(navController, padding)
                        }
                    }
                }
            }
        }
    }
}
